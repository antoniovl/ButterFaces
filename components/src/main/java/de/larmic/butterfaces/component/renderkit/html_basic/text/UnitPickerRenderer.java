package de.larmic.butterfaces.component.renderkit.html_basic.text;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import de.larmic.butterfaces.component.html.text.HtmlUnitPicker;
import de.larmic.butterfaces.component.partrenderer.InnerComponentWrapperPartRenderer;
import de.larmic.butterfaces.component.partrenderer.OuterComponentWrapperPartRenderer;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;

@FacesRenderer(componentFamily = HtmlUnitPicker.COMPONENT_FAMILY, rendererType = HtmlUnitPicker.RENDERER_TYPE)
public class UnitPickerRenderer extends AbstractHtmlTagRenderer<HtmlUnitPicker> {

   @Override
   public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
      super.encodeBegin(context, component, "butter-component-unitpicker");
   }

   @Override
   public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
      if (!component.isRendered()) {
         return;
      }

      final HtmlUnitPicker unitPicker = ((HtmlUnitPicker) component);
      final ResponseWriter writer = context.getResponseWriter();

      if (!unitPicker.isReadonly()) {
         super.encodeSuperEnd(context, component);

         writer.startElement("span", component);
         writer.writeAttribute("class", "input-group-addon", null);

         writer.startElement("span", component);
         writer.writeText("...", null);
         writer.endElement("span");

         writer.startElement("span", component);
         writer.writeAttribute("class", "glyphicon glyphicon-chevron-down", null);
         writer.endElement("span");

         writer.endElement("span");

         // Close inner component wrapper div
         new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(unitPicker, writer);

         // render tooltip elements if necessary
         renderTooltipIfNecessary(context, unitPicker);

         if (!unitPicker.isReadonly()) {
            writer.startElement("script", unitPicker);
            writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".input-group", createJQueryPluginCall(unitPicker)), null);
            writer.endElement("script");
         }

         // Open outer component wrapper div
         new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
      }
   }

   private String createJQueryPluginCall(HtmlUnitPicker unitPicker) {
      final StringBuilder jQueryPluginCall = new StringBuilder();

      jQueryPluginCall.append("unitpicker({");

      jQueryPluginCall.append("inputValue: 80,");
      jQueryPluginCall.append("unitEntries: [\"Tage\", \"Monate\", \"Jahre\"],");
      jQueryPluginCall.append("selectedUnitEntry: \"Monate\"");
      jQueryPluginCall.append("})");
      return jQueryPluginCall.toString();
   }
}
