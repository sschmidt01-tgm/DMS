package dms;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dms.model.User;

/**
 *
 * @author Samuel
 */
public class SimpleLoginView extends CustomComponent implements View, Button.ClickListener {

    public static final String NAME = "login";

    private final TextField user;

    private final PasswordField password;

    private final Button loginButton;

    private final VerticalLayout fields;

    private final VerticalLayout viewLayout;

    private Persist persist;

    public SimpleLoginView() {
        setSizeFull();
        user = new TextField("User:");
        user.setWidth("300px");
        user.setRequired(true);
        user.setInputPrompt("Your username (eg. jon)");
        user.setInvalidAllowed(false);

        password = new PasswordField("Password:");
        password.setWidth("300px");
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");

        loginButton = new Button("Login", this);

        fields = new VerticalLayout(user, password, loginButton);
        fields.setCaption("Please login to access the application. (root/toor)");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        user.focus(); // focus the username field when user arrives to the login view
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (user.isValid() || password.isValid()) {
            String username = user.getValue();
            String password = this.password.getValue();
            persist.begin();
            User u = persist.getUserWithCredentials(username, password);
            persist.commit();
            if (u != null) {
//                Store the current user in the service session
                getSession().setAttribute("user", u.getName());
//                Navigate to main view
                getUI().getNavigator().navigateTo(SimpleLoginMainView.NAME); // or MyVaadinUI.NAME
            }
        } else {
            // TODO add message : "Username or Password is wrong"
            this.user.setValue(null);
            this.password.setValue(null);
            this.user.focus();
        }
    }

    /**
     * @return the persist
     */
    public Persist getPersist() {
        return persist;
    }

    /**
     * @param persist the persist to set
     */
    public void setPersist(Persist persist) {
        this.persist = persist;
    }
}
