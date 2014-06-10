package dms;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import dms.model.User;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI {

    private Persist persist;

    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        persist = new Persist();
        persist.begin();

        User u = new User();
        u.setName("root");
        u.setPassword("toor");
        u.setAdmin(true);

        persist.saveEntity(u);

        persist.commit();

        persist.begin();
        u = persist.getUserWithCredentials("root", "toor");
        persist.commit();

        setNavigator(new Navigator(this, this));
        SimpleLoginView slv = new SimpleLoginView();
        slv.setPersist(persist);
        getNavigator().addView(SimpleLoginView.NAME, slv);
        SimpleLoginMainView slmv = new SimpleLoginMainView();
        slmv.setPersist(persist);
        getNavigator().addView(SimpleLoginMainView.NAME, slmv);
        
        getNavigator().addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof SimpleLoginView;
                if (!isLoggedIn && !isLoginView) {
                    //Redirect to login view always if a user has not yet logged in 
                    getNavigator().navigateTo(SimpleLoginView.NAME);
                    return false;

                } else if (isLoggedIn && isLoginView) {
                    //If someone tries to access to login view while logged in, then cancel 
                    return false;
                }
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
            }
        });

    }
}
