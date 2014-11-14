/**
 * jQuery-Plugin "Waiting panel popup" for ajax popup animation. If registered an waiting panel popup will be rendered
 * as long as ajax request is running.
 * Works with at least jQuery 1.3.2.
 *
 * How to use:
 * jQuery("#someSelector").waitingPanel();
 */
(function ($) {
    // extend jQuery --------------------------------------------------------------------
    var $waitingPanelDialog = null;
    var $waitingPanelDotSelector = null;
    var eventRegistered = false;
    var waitingPanelOpeningDelay = null;

    $.fn.waitingPanel = function (data) {

        function processAjaxUpdate() {
            var ajaxRequestRunning = false;

            // console.log('Setting waiting panel delay to ' + waitingPanelOpeningDelay);

            function showWaitingPanel() {
                $waitingPanelDialog.removeClass('butter-component-waitingPanel-hide');
                $waitingPanelDotSelector.startDots();
            }

            function processEvent(data) {
                if (data.status == 'begin') {
                    // console.log('Begin ajax event');
                    ajaxRequestRunning = true;
                    setTimeout(function () {
                        // console.log('Ajax request running: ' + ajaxRequestRunning);
                        if (ajaxRequestRunning) {
                            // console.log('Ajax request is running. Showing modal panel');
                            showWaitingPanel();
                        } else {
                            // console.log('Ajax request is not running. Not showing modal panel');
                        }

                    }, waitingPanelOpeningDelay);

                } else if (data.status == 'success') {
                    // console.log('End ajax event');
                    ajaxRequestRunning = false;
                    $waitingPanelDialog.addClass('butter-component-waitingPanel-hide');
                    $waitingPanelDotSelector.stopDots();
                }
            }

            return processEvent;
        }

        return this.each(function () {
            var $originalElement = $(this);
            var _elementId = $originalElement.attr('id');
            var _msg = document.getElementById(_elementId);

            $waitingPanelDialog = $(_msg);
            $waitingPanelDotSelector = $waitingPanelDialog.find('.butter-component-waitingPanel-processing');
            waitingPanelOpeningDelay = data.waitingPanelDelay;

            // I found no way to remove event listener from jsf js.
            // I tried to register a callback once and change it on render waiting panel but after this
            // no waiting panel appears anymore.
            // Actually on each rendering of this component a new callback is put on event listener collection.
            if (!eventRegistered) {
                // console.log('Register: ' + _elementId);

                jsf.ajax.addOnEvent(processAjaxUpdate());
                eventRegistered = true;
            }
        });

    };
}(jQuery));