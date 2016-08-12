package org.jboss.qa.tool.saatr.web.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.jboss.qa.tool.saatr.entity.Field;

/**
 * Panel for filling fields with values.
 * 
 * @author dsimko@redhat.com
 *
 */
@SuppressWarnings("serial")
public class FieldPanel extends GenericPanel<Field> {

    public FieldPanel(String id, final IModel<Field> model) {
        super(id, model);
        add(new Label("name"));
        AutoCompleteSettings settings = new AutoCompleteSettings();
        settings.setShowListOnEmptyInput(true);
        add(new AutoCompleteTextField<String>("value", new PropertyModel<String>(model, "value"), settings) {

            @Override
            protected Iterator<String> getChoices(String input) {
                List<String> choices = new ArrayList<>(10);
                for (final String option : model.getObject().getOptions()) {
                    if (option.startsWith(input.toUpperCase())) {
                        choices.add(option);
                        if (choices.size() == 10) {
                            break;
                        }
                    }
                }
                return choices.iterator();
            }
        });
    }
}