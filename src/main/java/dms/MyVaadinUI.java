package dms;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import dms.model.Document;
import dms.model.User;

@Theme("mytheme")
@SuppressWarnings("serial")
@Title("DMS by ayvazyan & schmidt")
public class MyVaadinUI extends UI {

    private Persist persist;

    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        persist = new Persist();
        User u = new User();
        
//        if (persist.getUserWithCredentials("root", "toor") != null ) {
//            persist.begin();
//
//            u.setName("root");
//            u.setPassword("toor");
//            u.setAdmin(true);
//
//            persist.saveEntity(u);
//
//            persist.commit();
//        }
        persist.begin();
        Document d = new Document();
        d.setAuthor(persist.getUserByUserName("root"));
        d.setCategory("kat");
        d.setComment("comment");
        persist.saveEntity(d);

        persist.begin();
        u = persist.getUserWithCredentials("root", "toor");
        persist.commit();

        setNavigator(new Navigator(this, this));
        SimpleLoginView slv = new SimpleLoginView();
        slv.setPersist(persist);
        getNavigator().addView(SimpleLoginView.NAME, slv);
        SimpleLoginMainView slmv = new SimpleLoginMainView(persist);
        getNavigator().addView(SimpleLoginMainView.NAME, slmv);

        getNavigator().addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof SimpleLoginView;
//                if(isLoggedIn){
//                    System.out.println("rekt = " );
//                    getNavigator().removeView(SimpleLoginMainView.NAME);
//                    SimpleLoginMainView slmv = new SimpleLoginMainView(persist);
//                    getNavigator().addView(SimpleLoginMainView.NAME, slmv);
//                }
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
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof SimpleLoginView;
            }
        });

    }
}
