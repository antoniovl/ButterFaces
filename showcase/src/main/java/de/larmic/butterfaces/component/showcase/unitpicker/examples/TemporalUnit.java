package de.larmic.butterfaces.component.showcase.unitpicker.examples;

import de.larmic.butterfaces.model.unitpicker.UnitValue;

public class TemporalUnit implements UnitValue {

   private String value;

   public TemporalUnit(String value) {
      this.value = value;
   }

   @Override
   public String getValue() {
      return value;
   }
}
