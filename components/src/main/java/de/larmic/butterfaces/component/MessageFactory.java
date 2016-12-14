package de.larmic.butterfaces.component;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Partial clone of {@link javax.faces.component.MessageFactory}, because we need some functionality from there, but it
 * is package private.
 */
public final class MessageFactory {

   public static final String REQUIRED_MESSAGE_ID = "de.larmic.butterfaces.component.value.REQUIRED";

   private MessageFactory() {
      // do nothing
   }

   /**
    * <p>Creates and returns a FacesMessage for the specified Locale.</p>
    *
    * @param context   - the <code>FacesContext</code> for the current request
    * @param messageId - the key of the message in the resource bundle
    * @param params    - substittion parameters
    *
    * @return a localized <code>FacesMessage</code> with the severity of FacesMessage.SEVERITY_ERROR
    */
   public static FacesMessage getMessage(FacesContext context,
                                         String messageId,
                                         Object... params) {

      if (context == null || messageId == null) {
         throw new NullPointerException(" context "
               + context
               + " messageId "
               + messageId);
      }
      Locale locale;
      // viewRoot may not have been initialized at this point.
      if (context.getViewRoot() != null) {
         locale = context.getViewRoot().getLocale();
      } else {
         locale = Locale.getDefault();
      }

      if (null == locale) {
         throw new NullPointerException(" locale is null ");
      }

      FacesMessage message = getMessage(locale, messageId, params);
      if (message != null) {
         return message;
      }
      locale = Locale.getDefault();
      return (getMessage(locale, messageId, params));
   }

   /**
    * <p>Creates and returns a FacesMessage for the specified Locale.</p>
    *
    * @param locale    - the target <code>Locale</code>
    * @param messageId - the key of the message in the resource bundle
    * @param params    - substittion parameters
    *
    * @return a localized <code>FacesMessage</code> with the severity of FacesMessage.SEVERITY_ERROR
    */
   public static FacesMessage getMessage(Locale locale,
                                         String messageId,
                                         Object... params) {
      ResourceBundle bundle = ResourceBundle.getBundle("Messages", locale);

      String summary = bundle.getString(messageId);
      String detail = null;
      if (bundle.containsKey(messageId + "_detail")) {
         detail = bundle.getString(messageId + "_detail");
      }

      // At this point, we have a summary and a bundle.
      FacesMessage ret = new BindingFacesMessage(locale, summary, detail, params);
      ret.setSeverity(FacesMessage.SEVERITY_ERROR);
      return (ret);
   }

   protected static Application getApplication() {
      FacesContext context = FacesContext.getCurrentInstance();
      if (context != null) {
         return (FacesContext.getCurrentInstance().getApplication());
      }
      ApplicationFactory afactory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
      return (afactory.getApplication());
   }

   protected static ClassLoader getCurrentLoader(Class fallbackClass) {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      if (loader == null) {
         loader = fallbackClass.getClassLoader();
      }
      return loader;
   }

   public static Object getLabel(FacesContext context, UIComponent component) {
      Object o = component.getAttributes().get("label");
      if (o == null || o instanceof String && ((String) o).length() == 0) {
         o = component.getValueExpression("label");
      }

      if (o == null) {
         o = component.getClientId(context);
      }

      return o;
   }

   /**
    * This class overrides FacesMessage to provide the evaluation
    * of binding expressions in addition to Strings.
    * It is often the case, that a binding expression may reference
    * a localized property value that would be used as a
    * substitution parameter in the message.  For example:
    * <code>#{bundle.userLabel}</code>
    * "bundle" may not be available until the page is rendered.
    * The "late" binding evaluation in <code>getSummary</code> and
    * <code>getDetail</code> allow the expression to be evaluated
    * when that property is available.
    */
   static class BindingFacesMessage extends FacesMessage {
      private Locale locale;
      private Object[] parameters;
      private Object[] resolvedParameters;

      BindingFacesMessage(
            Locale locale,
            String messageFormat,
            String detailMessageFormat,
            // array of parameters, both Strings and ValueBindings
            Object[] parameters) {

         super(messageFormat, detailMessageFormat);
         this.locale = locale;
         this.parameters = parameters;
         if (parameters != null) {
            resolvedParameters = new Object[parameters.length];
         }
      }

      public String getSummary() {
         String pattern = super.getSummary();
         resolveBindings();
         return getFormattedString(pattern, resolvedParameters);
      }

      public String getDetail() {
         String pattern = super.getDetail();
         resolveBindings();
         return getFormattedString(pattern, resolvedParameters);
      }

      private void resolveBindings() {
         FacesContext context = null;
         if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
               Object o = parameters[i];
               if (o instanceof ValueBinding) {
                  if (context == null) {
                     context = FacesContext.getCurrentInstance();
                  }
                  o = ((ValueBinding) o).getValue(context);
               }
               if (o instanceof ValueExpression) {
                  if (context == null) {
                     context = FacesContext.getCurrentInstance();
                  }
                  o = ((ValueExpression) o).getValue(context.getELContext());
               }
               // to avoid 'null' appearing in message
               if (o == null) {
                  o = "";
               }
               resolvedParameters[i] = o;
            }
         }
      }

      private String getFormattedString(String msgtext, Object[] params) {
         String localizedStr = null;

         if (params == null || msgtext == null) {
            return msgtext;
         }
         StringBuffer b = new StringBuffer(100);
         MessageFormat mf = new MessageFormat(msgtext);
         if (locale != null) {
            mf.setLocale(locale);
            b.append(mf.format(params));
            localizedStr = b.toString();
         }
         return localizedStr;
      }
   }
}
