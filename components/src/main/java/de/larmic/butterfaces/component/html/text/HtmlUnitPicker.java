package de.larmic.butterfaces.component.html.text;

import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;

import de.larmic.butterfaces.component.MessageFactory;
import de.larmic.butterfaces.component.html.InputComponentFacet;
import de.larmic.butterfaces.model.unitpicker.UnitPickerValue;
import de.larmic.butterfaces.model.unitpicker.UnitValue;
import de.larmic.butterfaces.util.StringUtils;

@ResourceDependencies({
      @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
      @ResourceDependency(library = "butterfaces-dist-bower", name = "jquery.js", target = "head"),
      @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.css", target = "head"),
      @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-unitpicker.css", target = "head"),
      @ResourceDependency(library = "butterfaces-js", name = "butterfaces-unitpicker.jquery.js", target = "head")
})
@FacesComponent(HtmlUnitPicker.COMPONENT_TYPE)
public class HtmlUnitPicker extends HtmlText {

   public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.unitpicker";
   public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
   public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.UnitPickerRenderer";

   protected static final String PROPERTY_UNIT_VALUES = "unitValues";
   protected static final String PROPERTY_PRESELECTED_UNIT_VALUE = "preSelectedUnitValue";

   private static final String REQUIRED_MESSAGE_ID = "de.larmic.butterfaces.component.unitpicker.REQUIRED";

   public HtmlUnitPicker() {
      super();
      this.setRendererType(RENDERER_TYPE);
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   @Override
   public List<InputComponentFacet> getSupportedFacets() {
      return Arrays.asList(InputComponentFacet.UNIT_PICKER);
   }

   @Override
   protected void validateValue(FacesContext context, Object newValue) {
      if (newValue != null && isRequired()) {
         UnitPickerValue unitPickerValue = (UnitPickerValue) newValue;
         if (isValueEmpty(unitPickerValue.getValue())
               || unitPickerValue.getUnitValue() == null
               || StringUtils.isEmpty(unitPickerValue.getUnitValue().getValue())) {
            String requiredMessageStr = getRequiredMessage();
            FacesMessage message;
            if (null != requiredMessageStr) {
               message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                     requiredMessageStr,
                     requiredMessageStr);
            } else {
               message =
                     MessageFactory.getMessage(context, REQUIRED_MESSAGE_ID,
                           MessageFactory.getLabel(
                                 context, this));
            }
            context.addMessage(getClientId(context), message);
            setValid(false);
         }
      }
      super.validateValue(context, newValue);
   }

   private boolean isValueEmpty(Object value) {
      return value == null || value instanceof String && "".equals(value);
   }

   public List<UnitValue> getUnitValues() {
      return (List<UnitValue>) getStateHelper().eval(PROPERTY_UNIT_VALUES);
   }

   public void setUnitValues(List<UnitValue> unitValues) {
      getStateHelper().put(PROPERTY_UNIT_VALUES, unitValues);
   }

   public UnitValue getPreSelectedUnitValue() {
      return (UnitValue) getStateHelper().eval(PROPERTY_PRESELECTED_UNIT_VALUE);
   }

   public void setPreSelectedUnitValue(UnitValue unitValue) {
      getStateHelper().put(PROPERTY_PRESELECTED_UNIT_VALUE, unitValue);
   }
}
