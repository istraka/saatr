
package org.jboss.qa.tool.saatr.web.comp.user;

import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jboss.qa.tool.saatr.domain.User;
import org.jboss.qa.tool.saatr.domain.build.Group;
import org.jboss.qa.tool.saatr.repo.UserRepository;
import org.jboss.qa.tool.saatr.repo.build.GroupRepository;

/**
 * @author dsimko@redhat.com
 *
 */
@SuppressWarnings("serial")
public class UserPanel extends GenericPanel<User> {

    @SpringBean
    private GroupRepository groupRepository;

    @SpringBean
    private UserRepository userRepository;

    public UserPanel(String id, final IModel<User> model) {
        super(id, new CompoundPropertyModel<>(model));
        add(new Label("username"));
        add(new Label("roles"));
        add(new RefreshingView<Group>("groups") {

            @Override
            protected Iterator<IModel<Group>> getItemModels() {
                return groupRepository.findAll().stream().map(g -> (IModel<Group>) new Model<>(g)).collect(Collectors.toList()).iterator();
            }

            @Override
            protected void populateItem(Item<Group> item) {
                item.add(new Label("name", item.getModelObject().getName()));
                item.add(new AjaxCheckBox("checkbox", new Model<Boolean>() {

                    @Override
                    public Boolean getObject() {
                        if (model.getObject() == null) {
                            return false;
                        }
                        return model.getObject().getGroups().contains(item.getModelObject());
                    }

                    @Override
                    public void setObject(Boolean bool) {
                        if (bool) {
                            model.getObject().getGroups().add(item.getModelObject());
                        } else {
                            model.getObject().getGroups().remove(item.getModelObject());
                        }
                        userRepository.save(model.getObject());
                    }
                }) {

                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        // nothing to refresh
                    }
                });
            }
        });
        add(new Link<User>("delete", model) {

            @Override
            public void onClick() {
                userRepository.delete(getModelObject());
                setModelObject(null);
            }
        });

    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisible(getModelObject() != null);
    }

}