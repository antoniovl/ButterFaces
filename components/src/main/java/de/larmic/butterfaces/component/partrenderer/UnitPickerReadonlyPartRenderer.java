package de.larmic.butterfaces.component.partrenderer;

import javax.faces.component.UIInput;

import de.larmic.butterfaces.model.unitpicker.UnitPickerValue;
import de.larmic.butterfaces.util.StringUtils;

public class UnitPickerReadonlyPartRenderer extends ReadonlyPartRenderer {
   @Override
   protected String generateCustomDisplayValue(Object value, UIInput component) {
      UnitPickerValue unitPickerValue = (UnitPickerValue) value;
      if (StringUtils.isEmpty(unitPickerValue.getUnitValue().getValue())) {
         return "-";
      } else {
         return unitPickerValue.getValue() + " " + unitPickerValue.getUnitValue().getValue();
      }
   }
}
