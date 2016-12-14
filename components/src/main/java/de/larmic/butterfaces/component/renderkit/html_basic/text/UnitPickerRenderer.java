package de.larmic.butterfaces.component.renderkit.html_basic.text;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;

import de.larmic.butterfaces.component.html.text.HtmlUnitPicker;
import de.larmic.butterfaces.component.partrenderer.InnerComponentWrapperPartRenderer;
import de.larmic.butterfaces.component.partrenderer.OuterComponentWrapperPartRenderer;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.UnitPickerReadonlyPartRenderer;
import de.larmic.butterfaces.model.unitpicker.UnitPickerValue;
import de.larmic.butterfaces.model.unitpicker.UnitValue;

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
      }

      // Open outer component wrapper div
      new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
   }

   @Override
   protected void encodeReadonly(HtmlUnitPicker htmlComponent, ResponseWriter writer) throws IOException {
      new UnitPickerReadonlyPartRenderer().renderReadonly(htmlComponent, writer);
   }

   @Override
   public Object getConvertedValue(final FacesContext context,
                                   final UIComponent component,
                                   final Object submittedValue) throws ConverterException {
      if (submittedValue == null || "".equals(submittedValue)) {
         return null;
      }

      final String submittedStringValue = (String) submittedValue;
      final HtmlUnitPicker unitPicker = (HtmlUnitPicker) component;

      final String[] valuePair = submittedStringValue.split("\\|");
      final String submittedUnitStringValue = valuePair[1];
      UnitValue submittedUnitValue = null;
      for (UnitValue unitValue : unitPicker.getUnitValues()) {
         if (unitValue.getValue().equals(submittedUnitStringValue)) {
            submittedUnitValue = unitValue;
            break;
         }
      }

      if (submittedUnitValue == null) {
         throw new ConverterException("couldn't find unitValue for '" + submittedUnitStringValue + "'");
      }

      return new UnitPickerValue(valuePair[0], submittedUnitValue);
   }

   private String createJQueryPluginCall(HtmlUnitPicker unitPicker) {
      final StringBuilder jQueryPluginCall = new StringBuilder();
      final UnitPickerValue unitPickerValue = (UnitPickerValue) unitPicker.getValue();

      jQueryPluginCall.append("unitpicker({");

      if (unitPicker.getPreSelectedUnitValue() != null && unitPickerValue.getValue() == null) {
         jQueryPluginCall.append("selectedUnitEntry: \"").append(unitPicker.getPreSelectedUnitValue().getValue()).append("\",");
      } else if (unitPicker.getValue() != null) {
         jQueryPluginCall.append("inputValue: \"").append(unitPickerValue.getValue()).append("\",");
         jQueryPluginCall.append("selectedUnitEntry: \"").append(unitPickerValue.getUnitValue().getValue()).append("\",");
      }

      jQueryPluginCall.append("unitEntries: ").append(renderUnitValuesString(unitPicker.getUnitValues())).append(",");

      jQueryPluginCall.append("})");
      return jQueryPluginCall.toString();
   }

   private String renderUnitValuesString(List<UnitValue> unitValues) {
      StringBuilder sb = new StringBuilder();
      sb.append("[");
      for (UnitValue unitValue : unitValues) {
         sb.append("\"").append(unitValue.getValue()).append("\",");
      }
      sb.append("]");
      return sb.toString();
   }
}
