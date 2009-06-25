// Eval Template Facebox JS for extending the facebox functionality
// @author lovemore.nalube@uct.ac.za

var evalTemplateFacebox = (function() {

    //backup the facebox functions
    var origionalFBfunctions = {
        loading: $.facebox.loading,
        close: $.facebox.close,
        reveal: $.facebox.reveal
    };

    var initFunction = function() {
        //Override FB settings or Extend methods/actions

        $.facebox.settings.opacity = 0.1;
        $.facebox.settings.overlay = true;
        $.facebox.settings.loadingImage = '/library/image/sakai/spinner.gif';
        $.facebox.settings.closeImage = '/library/image/sakai/cross.png';

        $.facebox.settings.elementToUpdate = null; //this is to store the parent element of item being edited to update after an edit via ajax
        $.facebox.saveOrder = function(f) {
            $('#saveReorderButton').click();
            $.facebox({ajax:(f)});
        };
        $.facebox.settings.faceboxHtml = '\
        <div id="facebox" style="display:none;"> \
          <div class="popup"> \
            <table width="700px"> \
              <tbody> \
                <tr> \
                  <td class="tl"/><td class="b"/><td class="tr"/> \
                </tr> \
                <tr> \
                  <td class="b"/> \
                  <td class="body"> \
                  <div class="header breadCrumb" style="display:block"> \
                 <a class="close" href="#" accesskey="x"><img class="close_image" title="close"/></a></div> \
                    <div style="display: none" class="results"></div> \
                    <div class="content"> \
                    </div> \
                  </td> \
                  <td class="b"/> \
                </tr> \
                <tr> \
                  <td class="bl"/><td class="b"/><td class="br"/> \
                </tr> \
              </tbody> \
            </table> \
          </div> \
        </div>';

        $.facebox.loading = function() {
            origionalFBfunctions.loading();
            $('#saveReorderButton').click();
            $('#facebox').css({
                left:    $(window).width() / 2 - ($('#facebox table').width() / 2)
            }).show();
            $('#facebox_overlay').unbind('click');
        };

        $.facebox.reveal = function(data, klass) {
            $('#facebox .content').empty();
            $("#facebox .results").empty();
            var fbCssLeft = $('#facebox').css('left');
            origionalFBfunctions.reveal(data, klass);
            //restore left css value
            $('#facebox').css('left', fbCssLeft);
            evalTemplateUtils.frameGrow(0); //TODO: find a suitable height
        };

        $.facebox.close = function() {
            origionalFBfunctions.close();
            evalTemplateUtils.debug.info("calling stiff");
            evalTemplateUtils.frameShrink(0); //TODO: find a suitable height
            evalTemplateFacebox.fbResetClasses();
            $('#facebox table').attr('width', 700);
            $('#facebox .body').css('width', 660);
            $('#facebox .header').eq(0).show();
            $.facebox.settings.elementToUpdate = null;
            return false;
        };

        $.facebox.setHeader = function(obj) {
            if ($("#facebox .titleHeader")) {
                $("#facebox .titleHeader").remove();
            }
            $(obj).clone(true).insertBefore($("#facebox .header .close"));
            $(obj).remove();
        };

        $.facebox.setBtn = function(obj, form) {
            var btn = '<input type="button" class="active" accesskey="s" />';
            $(btn).insertBefore($("#facebox .close"));
            $("#facebox .header .active").val(obj);
            $("#facebox .header .active").click(function() {
                $(form).trigger('click');
            });
        };

        $.facebox.showResponse = function(entityCat, id) {
            evalTemplateData.fillActionResponse(entityCat, id);
        };

    };


    return {
        init: function() {
            initFunction();
        },
        fbResetClasses: function() {
            $("#itemList").find(".editing").attr("class", "itemRow");
            if ($("#facebox .titleHeader").length > 0) {
                $("#facebox .titleHeader").remove();
            }
        }
    };


})
        ($);

/*This is a fix for the Interface Highlight compatibility bug [added by lovemore.nalube@uct.ac.za]
 * see http://groups.google.com/group/jquery-en/browse_thread/thread/d094a3f299055a99
 */
( function($) {
    $.dequeue = function(a, b) {
        return $(a).dequeue(b);
    };
})(jQuery);

/**
 attributes with the rel=faceboxGrid have pre-click events attached to them
 **/

(function($) {
    function setPreClick(that) {
        that.each(function() {
            $(this).bind('click', function() {
                evalTemplateFacebox.fbResetClasses();
                if ($(this).attr("rel") == "faceboxGrid") {
                    $(this).parent().parent().parent().parent().attr("class", "editing");
                    $.facebox.settings.elementToUpdate = $(this).parent().parent().parent();
                }
                $.facebox({ajax: this.href});
                return false;
            });
        });
    }

    $.fn.faceboxGrid = function() {
        setPreClick($(this));
    };
})(jQuery);