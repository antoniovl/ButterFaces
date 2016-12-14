/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.model.unitpicker;

import java.util.Objects;

public class EnumUnitPickerWrapper implements UnitValue {

   private final Enum enumValue;
   private final String value;

   public EnumUnitPickerWrapper(Enum enumValue, String value) {
      assert enumValue != null;
      this.enumValue = enumValue;
      this.value = value;
   }

   public Enum getEnumValue() {
      return enumValue;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }

      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      EnumUnitPickerWrapper that = (EnumUnitPickerWrapper) o;
      return Objects.equals(getEnumValue(), that.getEnumValue());
   }

   @Override
   public int hashCode() {
      return Objects.hash(getEnumValue());
   }

   @Override
   public String toString() {
      final StringBuffer sb = new StringBuffer("EnumUnitPickerWrapper{");
      sb.append("enumValue=").append(enumValue);
      sb.append(", value='").append(value).append('\'');
      sb.append('}');
      return sb.toString();
   }

   @Override
   public String getValue() {
      return value;
   }
}
