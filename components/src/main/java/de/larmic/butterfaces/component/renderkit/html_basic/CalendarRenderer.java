package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.HtmlBasicInputRenderer;
import de.larmic.butterfaces.component.html.HtmlCalendar;
import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlCalendar.COMPONENT_FAMILY, rendererType = HtmlCalendar.RENDERER_TYPE)
public class CalendarRenderer extends HtmlBasicInputRenderer {

    private static final Attribute[] INPUT_ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.INPUTTEXT);

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        super.encodeBegin(context, component);

        final HtmlInputComponent htmlComponent = (HtmlInputComponent) component;
        final ResponseWriter writer = context.getResponseWriter();

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentBegin(htmlComponent, writer);

        // Render label if components label attribute is set
        new LabelPartRenderer().renderLabel(htmlComponent, writer);

        // Open inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperBegin(htmlComponent, writer);

        // Render readonly span if components readonly attribute is set
        new ReadonlyPartRenderer().renderReadonly(htmlComponent, writer);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlCalendar calendar = (HtmlCalendar) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!calendar.isReadonly()) {
            super.encodeEnd(context, component);
            if (calendar.isPickDate() || calendar.isPickTime()) {
                writer.startElement("span", component);
                writer.writeAttribute("class", "input-group-addon", null);
                writer.startElement("span", component);
                // jquery plugin will add icon here
                writer.endElement("span");
                writer.endElement("span");
            }
        }

        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(calendar, writer);

        // render tooltip elements if necessary
        new TooltipPartRenderer().renderTooltip(calendar, writer);

        if (calendar.isPickDate() || calendar.isPickTime()) {
            writer.startElement("script", calendar);
            writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".input-group", createJQueryPluginCall(calendar)), null);
            writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".input-group", createJQueryPluginCallback(calendar)), null);
            writer.endElement("script");
        }

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    private String createJQueryPluginCall(HtmlCalendar calendar) {
        final StringBuilder jQueryPluginCall = new StringBuilder();

        final String calendarDate = StringUtils.getNotNullValue(calendar.getGlyphiconDate(), "glyphicon glyphicon-calendar");
        final String calendarTime = StringUtils.getNotNullValue(calendar.getGlyphiconTime(), "glyphicon glyphicon-time");
        final String calendarUp = StringUtils.getNotNullValue(calendar.getGlyphiconUp(), "glyphicon glyphicon-chevron-up");
        final String calendarDown = StringUtils.getNotNullValue(calendar.getGlyphiconDown(), "glyphicon glyphicon-chevron-down");

        jQueryPluginCall.append("datetimepicker({");
        jQueryPluginCall.append("pickTime: " + calendar.isPickTime() + ",");
        jQueryPluginCall.append("pickDate: " + calendar.isPickDate() + ",");
        jQueryPluginCall.append("sideBySide: " + calendar.isSideBySide() + ",");
        jQueryPluginCall.append("icons: {");
        jQueryPluginCall.append("time: '" + calendarTime + "',");
        jQueryPluginCall.append("date: '" + calendarDate + "',");
        jQueryPluginCall.append("up: '" + calendarUp + "',");
        jQueryPluginCall.append("down: '" + calendarDown + "'");
        jQueryPluginCall.append("},");
        jQueryPluginCall.append("language: \"" + calendar.getLanguage() + "\"");
        jQueryPluginCall.append("})");
        return jQueryPluginCall.toString();
    }

    private String createJQueryPluginCallback(HtmlCalendar calendar) {
        final StringBuilder jQueryPluginCall = new StringBuilder();
        jQueryPluginCall.append("on(\"dp.change\", function (e) {");
        jQueryPluginCall.append(RenderUtils.createJQueryBySelector(calendar.getClientId(), ".butter-input-component") + "trigger('keyup');");
        jQueryPluginCall.append("})");
        return jQueryPluginCall.toString();
    }

    /**
     * Method copied from super class to add html features.
     */
    @Override
    protected void getEndTextToRender(final FacesContext context, final UIComponent component, final String currentValue)
            throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        if (component instanceof UIInput) {
            writer.startElement("input", component);

            writer.writeAttribute("name", (component.getClientId(context)), "clientId");

            // only output the autocomplete attribute if the value
            // is 'off' since its lack of presence will be interpreted
            // as 'on' by the browser
            if ("off".equals(component.getAttributes().get("autocomplete"))) {
                writer.writeAttribute("autocomplete", "off", "autocomplete");
            }

            // render default text specified
            if (currentValue != null) {
                writer.writeAttribute("value", currentValue, "value");
            }

            final String styleClass = StringUtils.concatWithSpace(Constants.INPUT_COMPONENT_MARKER,
                    Constants.BOOTSTRAP_FORM_CONTROL,
                    !((HtmlInputComponent) component).isValid() ? Constants.INVALID_STYLE_CLASS : null);

            if (StringUtils.isNotEmpty(styleClass)) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }

            // *** BEGIN HTML 5 CHANGED **************************
            this.renderHtmlFeatures(component, writer);
            // *** END HTML 5 CHANGED ****************************

            // style is rendered as a passthur attribute
            RenderKitUtils.renderPassThruAttributes(context, writer, component, INPUT_ATTRIBUTES,
                    getNonOnChangeBehaviors(component));
            RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

            RenderKitUtils.renderOnchange(context, component, false);

            writer.endElement("input");
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {

        boolean renderChildren = WebConfiguration.getInstance()
                .isOptionEnabled(WebConfiguration.BooleanWebContextInitParameter.AllowTextChildren);

        if (!renderChildren) {
            return;
        }

        rendererParamsNotNull(context, component);

        if (!shouldEncodeChildren(component)) {
            return;
        }

        if (component.getChildCount() > 0) {
            for (UIComponent kid : component.getChildren()) {
                encodeRecursive(context, kid);
            }
        }

    }

    protected void renderHtmlFeatures(UIComponent component, ResponseWriter writer) throws IOException {
        if (component instanceof HtmlCalendar) {
            final HtmlCalendar inputComponent = (HtmlCalendar) component;

            final HtmlAttributePartRenderer htmlAttributePartRenderer = new HtmlAttributePartRenderer();

            htmlAttributePartRenderer.writePlaceholderAttribute(writer, inputComponent.getPlaceholder());

            if (inputComponent.getAutoFocus()) {
                writer.writeAttribute("autofocus", "true", null);
            }
        } else {
            writer.writeAttribute("type", "text", null);
        }
    }


}
