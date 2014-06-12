package dms;

import com.FileAssist;
import com.MyDownloadButton;
import com.MyUploader;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import dms.model.Document;
import dms.model.User;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Samuel
 */
public class SimpleLoginMainView extends CustomComponent implements View {

    public static final String NAME = "";

    private Persist persist;

    private Table contactList = new Table();
    private TextField searchField = new TextField();
    private Button addNewContactButton = new Button("New Document");
    private Button removeContactButton = new Button("Remove this document");
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();

    private static final String DAUTHOR = "Autor";
    private static final String DKAT = "Kategorie";
    private static final String DKOM = "Kommentar";
    private static final String DNAME = "Name";
    private static final String DTYPE = "Type";
    private static final String DKEY = "Keywords";
    private static final String[] fieldNames = new String[]{DAUTHOR, DKAT,
        DKOM, DNAME, DTYPE, DKEY};

    Button logout = new Button("Logout", new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
//            "Logout" the user
            getSession().setAttribute("user", null);
//            Refresh this view, should redirect to login view
            persist.close();
            getUI().getNavigator().navigateTo(NAME);
        }
    });
    
    public SimpleLoginMainView(Persist persist) {
        this.persist = persist;
        contactContainer = createDummyDatasource();
        init();
    }

    @Override
    public void enter(ViewChangeEvent event) {
        contactContainer = createDummyDatasource();
        init();
    }

    /*
     * Any component can be bound to an external data source. This example uses
     * just a dummy in-memory list, but there are many more practical
     * implementations.
     */
    IndexedContainer contactContainer;

    /*
     * After UI class is created, init() is executed. You should build and wire
     * up your user interface here.
     */
    protected void init() {
        initLayout();
        initContactList();
        initEditor();
        initSearch();
        initAddRemoveButtons();
    }

    /*
     * In this example layouts are programmed in Java. You may choose use a
     * visual editor, CSS or HTML templates for layout instead.
     */
    private void initLayout() {

        /* Root of the user interface component tree is set */
        VerticalLayout vertical = new VerticalLayout();

        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        setCompositionRoot(vertical);

        // Add the topmost component.

        /* Build the component tree */
        VerticalLayout leftLayout = new VerticalLayout();
        splitPanel.addComponent(leftLayout);
        splitPanel.addComponent(editorLayout);
        leftLayout.addComponent(contactList);
        HorizontalLayout bottomLeftLayout = new HorizontalLayout();
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(searchField);
        bottomLeftLayout.addComponent(addNewContactButton);

        /* Set the contents in the left of the split panel to use all the space */
        leftLayout.setSizeFull();

        /*
         * On the left side, expand the size of the contactList so that it uses
         * all the space left after from bottomLeftLayout
         */
        leftLayout.setExpandRatio(contactList, 1);
        contactList.setSizeFull();

        /*
         * In the bottomLeftLayout, searchField takes all the width there is
         * after adding addNewContactButton. The height of the layout is defined
         * by the tallest component.
         */
        bottomLeftLayout.setWidth("100%");
        searchField.setWidth("100%");
        bottomLeftLayout.setExpandRatio(searchField, 1);

        /* Put a little margin around the fields in the right side editor */
        editorLayout.setMargin(true);
        editorLayout.setVisible(false);

        vertical.addComponent(splitPanel);
        HorizontalLayout hl = new HorizontalLayout(logout);
        hl.setWidth("100%");
        hl.setSizeFull();
        hl.setExpandRatio(logout, 1);
        vertical.addComponent(hl);

    }

    private void initEditor() {

        editorLayout.addComponent(removeContactButton);

        /* User interface can be created dynamically to reflect underlying data. */
        for (String fieldName : fieldNames) {
            TextField field = new TextField(fieldName);
            editorLayout.addComponent(field);
            field.setWidth("100%");

            /*
             * We use a FieldGroup to connect multiple components to a data
             * source at once.
             */
            editorFields.bind(field, fieldName);
        }

        MyUploader myup = new MyUploader();
        myup.init();
        editorLayout.addComponent(myup);

        String[] listOfFiles = FileAssist.getFileNames();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                Button btn = new MyDownloadButton(listOfFiles[i]);
                editorLayout.addComponent(btn);
            }
        }

        /*
         * Data can be buffered in the user interface. When doing so, commit()
         * writes the changes to the data source. Here we choose to write the
         * changes automatically without calling commit().
         */
        editorFields.setBuffered(false);
    }

    private void initSearch() {

        /*
         * We want to show a subtle prompt in the search field. We could also
         * set a caption that would be shown above the field or description to
         * be shown in a tooltip.
         */
        searchField.setInputPrompt("Search documents");

        /*
         * Granularity for sending events over the wire can be controlled. By
         * default simple changes like writing a text in TextField are sent to
         * server with the next Ajax call. You can set your component to be
         * immediate to send the changes to server immediately after focus
         * leaves the field. Here we choose to send the text over the wire as
         * soon as user stops writing for a moment.
         */
        searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

        /*
         * When the event happens, we handle it in the anonymous inner class.
         * You may choose to use separate controllers (in MVC) or presenters (in
         * MVP) instead. In the end, the preferred application architecture is
         * up to you.
         */
        searchField.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(final TextChangeEvent event) {

                /* Reset the filter for the contactContainer. */
                contactContainer.removeAllContainerFilters();
                contactContainer.addContainerFilter(new ContactFilter(event
                        .getText()));
            }
        });
    }

    /*
     * A custom filter for searching names and companies in the
     * contactContainer.
     */
    private class ContactFilter implements Filter {

        private final String needle;

        public ContactFilter(String needle) {
            this.needle = needle.toLowerCase();
        }

        @Override
        public boolean passesFilter(Object itemId, Item item) {
            String haystack = ("" + item.getItemProperty(DAUTHOR).getValue()
                    + item.getItemProperty(DKAT).getValue() + item
                    .getItemProperty(DKOM).getValue() + item.getItemProperty(DNAME).getValue()
                    + item.getItemProperty(DTYPE).getValue() + item.getItemProperty(DKEY).getValue()).toLowerCase();
            return haystack.contains(needle);
        }

        @Override
        public boolean appliesToProperty(Object id) {
            return true;
        }
    }

    private void initAddRemoveButtons() {
        addNewContactButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                /*
                 * Rows in the Container data model are called Item. Here we add
                 * a new row in the beginning of the list.
                 */
                contactContainer.removeAllContainerFilters();
                Object contactId = contactContainer.addItemAt(0);

                /*
                 * Each Item has a set of Properties that hold values. Here we
                 * set a couple of those.
                 */
                contactList.getContainerProperty(contactId, DAUTHOR).setValue(
                        "New");
                contactList.getContainerProperty(contactId, DKAT).setValue(
                        "Document");

                /* Lets choose the newly created contact to edit it. */
                contactList.select(contactId);
            }
        });

        removeContactButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Object contactId = contactList.getValue();
                contactList.removeItem(contactId);
            }
        });
    }

    private void initContactList() {
        contactList.setContainerDataSource(contactContainer);
        contactList.setVisibleColumns(new String[]{DAUTHOR, DKAT, DKOM, DNAME, DTYPE, DKEY});
        contactList.setSelectable(true);
        contactList.setImmediate(true);

        contactList.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                Object contactId = contactList.getValue();

                /*
                 * When a contact is selected from the list, we want to show
                 * that in our editor on the right. This is nicely done by the
                 * FieldGroup that binds all the fields to the corresponding
                 * Properties in our contact at once.
                 */
                if (contactId != null) {
                    editorFields.setItemDataSource(contactList
                            .getItem(contactId));
                }

                editorLayout.setVisible(contactId != null);
            }
        });
    }

    /*
     * Generate some in-memory example data to play with. In a real application
     * we could be using SQLContainer, JPAContainer or some other to persist the
     * data.
     */
    private IndexedContainer createDummyDatasource() {
        IndexedContainer ic = new IndexedContainer();

        for (String p : fieldNames) {
            ic.addContainerProperty(p, String.class, "");
        }

        List<Document> d;
        persist.begin();
        d = persist.getDocuments();
        persist.commit();
        String test = "";
        if (getSession() != null) {
            test = (String) getSession().getAttribute("user");
        }
        persist.begin();
        User u = persist.getUserByUserName(test);
        persist.commit();
        for (int i = 0; i < d.size(); i++) {
            if (u != null && u.isAdmin()) {
                Object id = ic.addItem();
                Document temp = (Document) d.get(i);
//                Collection<User> temp2 = temp.getUsers();
//                for(User temp3: temp2){
//                    temp3.getName().equals(u.getName());
//                }
                ic.getContainerProperty(id, DAUTHOR).setValue(
                        temp.getAuthor().getName());
                ic.getContainerProperty(id, DKAT).setValue(
                        temp.getCategory());
                ic.getContainerProperty(id, DNAME).setValue(
                        temp.getDocumentName());
                ic.getContainerProperty(id, DTYPE).setValue(
                        temp.getFileEnding());
                ic.getContainerProperty(id, DKEY).setValue(
                        temp.getKeywords());
            }
        }

        return ic;
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
