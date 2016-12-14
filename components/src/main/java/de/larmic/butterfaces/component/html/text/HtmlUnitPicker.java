package de.larmic.butterfaces.component.html.text;

import java.util.Arrays;
import java.util.List;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;

import de.larmic.butterfaces.component.html.InputComponentFacet;
import de.larmic.butterfaces.model.unitpicker.UnitValue;

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

   public List<UnitValue> getUnitValues() {
      return (List<UnitValue>) getStateHelper().eval(PROPERTY_UNIT_VALUES);
   }

   public void setUnitValues(List<UnitValue> unitValues) {
      getStateHelper().put(PROPERTY_UNIT_VALUES, unitValues);
   }
}
