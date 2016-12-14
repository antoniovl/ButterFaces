/**
 * butterItemFilterField is a jQuery plugin that filters html element with the css class <code>filterable-item</code>.
 * It is applied to the search field.<br/>
 * If no filter text is entered, then all filterable-items are displayed. Else the search field value is matched against <b>all</b> text contained by a filterable-item.
 *
 * How to use:
 * jQuery("#someInputSelector").butterItemFilterField();
 *
 * Author: Stephan Zerhusen
 */
(function ($) {

    var UnitPicker = function (rootElement, options) {
        this._requireOptionsNotUndefined(options);

        this._options = options;
        this._entries = options.unitEntries;
        this._inputValue = options.inputValue;

        this._$root = $(rootElement);
        this._$originalInput = this._$root.find("input");
        this._$clonedInput = this._createAndAppendInputClone(this._$originalInput);
        this._$selectedEntryLabel = this._$root.find("span span:first-child").addClass("butter-component-unitpicker-unitlabel");
        this._$unitsPopup = null;

        if (options.selectedUnitEntry) {
            this._$selectedEntryLabel.text(options.selectedUnitEntry);
        } else {
            this._$selectedEntryLabel.text(this._entries[0]);
        }
        this._initInputField();
        this._synchValuesToOriginalInput();

        this._initDropDownButton();
    };

    UnitPicker.prototype = {
        _requireOptionsNotUndefined: function (options) {
            if (options === undefined) {
                throw new Error("UnitPicker: options may not be undefined");
            }
        },

        _createAndAppendInputClone: function ($input) {
            var $clonedInput = $("<input>")
                    .addClass($input.attr("class"))
                    .attr("style", $input.attr("style"))
                    .attr("placeholder", $input.attr("placeholder"))
                    .attr("type", $input.attr("type"));
            this._$originalInput.before($clonedInput);
            $input.hide();
            return $clonedInput;
        },

        _initInputField: function () {
            var that = this;
            if (that._inputValue) {
                that._$clonedInput.val(that._inputValue);
            }

            that._$clonedInput
                    .blur(function (event) {
                        that._hideUnitsPopup();
                    })
                    .keydown(function (event) {
                        if (event.which === 27) {
                            that._hideUnitsPopup();
                        }
                    })
                    .bind("input propertychange change", function (event) {
                        that._synchValuesToOriginalInput();
                        if("change" === event.type.toLowerCase()) {
                            // trigger change event on original for JSF
                            that._$originalInput.change();
                        }
                    });
        },

        _initDropDownButton: function () {
            var that = this;
            that._$unitPickerButton = that._$root.find(".input-group-addon")
                    .addClass("butter-component-unitpicker-btn")
                    .mousedown(function (event) {
                        event.preventDefault();
                    })
                    .click(function (event) {
                        if (that._isPopupShown()) {
                            that._hideUnitsPopup();
                        } else {
                            that._showUnitsPopup();
                        }
                    });
        },

        _showUnitsPopup: function () {
            var that = this;

            that._$unitsPopup = $("<div>")
                    .addClass("butter-component-unitpicker-popup")
                    .css({
                        top: that._$unitPickerButton.outerHeight() - 1,
                        right: 0,
                        minWidth: that._$unitPickerButton.outerWidth() + 1
                    });

            var $listContainer = $("<ul>").appendTo(that._$unitsPopup);
            that._entries.forEach(function (entry) {
                $("<li>")
                        .text(entry)
                        .mousedown(function (event) {
                            // prevent triggering blur event on input, otherwise our click handler
                            // wouldn_t be triggered
                            event.preventDefault();
                        })
                        .click(function (event) {
                            that._$selectedEntryLabel.text($(this).text());
                            that._synchValuesToOriginalInput();
                            // causes popup to hide indirectly
                            that._$clonedInput.blur();
                            // trigger change event on original for JSF
                            that._$originalInput.change();
                        })
                        .appendTo($listContainer);
            });

            that._$unitsPopup.appendTo(that._$root);

            that._$clonedInput.focus();
        },

        _hideUnitsPopup: function () {
            var that = this;

            if (that._isPopupShown()) {
                that._$unitsPopup.remove();
                that._$unitsPopup = null;
            }
        },

        _isPopupShown: function () {
            return this._$unitsPopup !== null;
        },

        _synchValuesToOriginalInput: function () {
            this._$originalInput.val(this._$clonedInput.val() + "|" + this._$selectedEntryLabel.text());
        }
    };

    $.fn.unitpicker = function (options) {
        return this.each(function () {
            new UnitPicker(this, options);
        });
    };

}(jQuery));