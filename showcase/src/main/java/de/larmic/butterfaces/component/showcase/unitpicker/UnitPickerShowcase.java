package de.larmic.butterfaces.component.showcase.unitpicker;

import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import de.larmic.butterfaces.component.showcase.AbstractInputShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.util.StringUtils;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class UnitPickerShowcase extends AbstractInputShowcase implements Serializable {

   private static final long serialVersionUID = -3875787228262221910L;

   private String placeholder = "Enter value...";
   private boolean autoFocus;

   @Override
   protected Object initValue() {
      return null;
   }

   @Override
   public String getReadableValue() {
      return (String) this.getValue();
   }

   @Override
   public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {

      final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

      xhtmlCodeExample.appendInnerContent("        <b:unitpicker id=\"input\"");
      xhtmlCodeExample.appendInnerContent("                    label=\"" + getLabel() + "\"");
      xhtmlCodeExample.appendInnerContent("                    hideLabel=\"" + isHideLabel() + "\"");
      xhtmlCodeExample.appendInnerContent("                    value=\"" + getValue() + "\"");
      xhtmlCodeExample.appendInnerContent("                    placeholder=\"" + getPlaceholder() + "\"");
      xhtmlCodeExample.appendInnerContent("                    styleClass=\"" + getStyleClass() + "\"");
      xhtmlCodeExample.appendInnerContent("                    readonly=\"" + isReadonly() + "\"");
      xhtmlCodeExample.appendInnerContent("                    required=\"" + isRequired() + "\"");
      xhtmlCodeExample.appendInnerContent("                    disabled=\"" + this.isDisabled() + "\"");
      xhtmlCodeExample.appendInnerContent("                    autoFocus=\"" + isAutoFocus() + "\"");
      xhtmlCodeExample.appendInnerContent("                    rendered=\"" + isRendered() + "\">");

      addAjaxTag(xhtmlCodeExample, "change");

      if (StringUtils.isNotEmpty(getTooltip())) {
         xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
         xhtmlCodeExample.appendInnerContent("                " + getTooltip());
         xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
      }

      xhtmlCodeExample.appendInnerContent("        </b:calunitpickerendar>", false);

      addOutputExample(xhtmlCodeExample);

      codeExamples.add(xhtmlCodeExample);
   }

   public String getPlaceholder() {
      return this.placeholder;
   }

   public void setPlaceholder(final String placeholder) {
      this.placeholder = placeholder;
   }

   public boolean isAutoFocus() {
      return autoFocus;
   }

   public void setAutoFocus(boolean autoFocus) {
      this.autoFocus = autoFocus;
   }
}
