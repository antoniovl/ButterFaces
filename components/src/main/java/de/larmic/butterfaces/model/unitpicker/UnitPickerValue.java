package de.larmic.butterfaces.model.unitpicker;

import java.util.Objects;

public class UnitPickerValue {

   private Object value;
   private UnitValue unitValue;

   public UnitPickerValue() {
      // default
   }

   public UnitPickerValue(Object value, UnitValue unitValue) {
      this.value = value;
      this.unitValue = unitValue;
   }

   public Object getValue() {
      return value;
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public UnitValue getUnitValue() {
      return unitValue;
   }

   public void setUnitValue(UnitValue unitValue) {
      this.unitValue = unitValue;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }
      UnitPickerValue that = (UnitPickerValue) o;
      return Objects.equals(getValue(), that.getValue()) &&
            Objects.equals(getUnitValue(), that.getUnitValue());
   }

   @Override
   public int hashCode() {
      return Objects.hash(getValue(), getUnitValue());
   }

   @Override
   public String toString() {
      final StringBuffer sb = new StringBuffer("UnitPickerValue{");
      sb.append("value=").append(value);
      sb.append(", unitValue=").append(unitValue);
      sb.append('}');
      return sb.toString();
   }
}
