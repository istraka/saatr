
package org.jboss.qa.tool.saatr.web.comp.build;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.extensions.markup.html.repeater.tree.TableTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.content.Folder;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.theme.HumanTheme;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jboss.qa.tool.saatr.domain.build.BuildDocument;
import org.jboss.qa.tool.saatr.repo.build.BuildRepository;
import org.jboss.qa.tool.saatr.web.comp.DocumentModel;

/**
 * @author dsimko@redhat.com
 */
@SuppressWarnings("serial")
public class BuildsTreeTablePanel extends GenericPanel<BuildDocument> {

    private TableTree<BuildDocument, String> tree;

    public BuildsTreeTablePanel(String id, IModel<BuildDocument> model, IModel<BuildFilter> filterModel) {
        super(id, model);

        List<IColumn<BuildDocument, String>> columns = new ArrayList<>();
        columns.add(new TreeColumn<BuildDocument, String>(Model.of("Job Name")));
        columns.add(new PropertyColumn<BuildDocument, String>(Model.of("Build Number"), "buildNumber"));
        columns.add(new StatusColumn());

        tree = new TableTree<BuildDocument, String>("tree", columns, new BuildsProvider(filterModel), Integer.MAX_VALUE, new BuildsExpansionModel()) {

            @Override
            protected Component newContentComponent(String id, IModel<BuildDocument> model) {
                return new Folder<BuildDocument>(id, tree, model) {

                    @Override
                    protected MarkupContainer newLinkComponent(String id, IModel<BuildDocument> model) {
                        BuildDocument foo = model.getObject();
                        if (tree.getProvider().hasChildren(foo)) {
                            return super.newLinkComponent(id, model);
                        } else {
                            return new Link<BuildDocument>(id, model) {

                                public void onClick() {
                                    BuildsTreeTablePanel.this.setModelObject(getModelObject());
                                }
                            };
                        }
                    }

                    @Override
                    protected boolean isSelected() {
                        return BuildsTreeTablePanel.this.getModelObject() != null && BuildsTreeTablePanel.this.getModelObject().equals(getModelObject());
                    }

                    @Override
                    protected IModel<?> newLabelModel(IModel<BuildDocument> model) {
                        return new PropertyModel<>(model, "jobName");
                    }
                };

            }

            @Override
            protected Item<BuildDocument> newRowItem(String id, int index, IModel<BuildDocument> model) {
                return new OddEvenItem<>(id, index, model);
            }

        };
        tree.getTable().addTopToolbar(new HeadersToolbar<>(tree.getTable(), null));
        tree.getTable().addBottomToolbar(new NoRecordsToolbar(tree.getTable()));
        tree.add(new HumanTheme());
        add(tree);
        add(new Link<Void>("expandAll") {

            @Override
            public void onClick() {
                BuildExpansion.get().expandAll();
            }
        });

        add(new Link<Void>("collapseAll") {

            @Override
            public void onClick() {
                BuildExpansion.get().collapseAll();
            }
        });
    }

    private class BuildsExpansionModel implements IModel<Set<BuildDocument>> {

        @Override
        public Set<BuildDocument> getObject() {
            return BuildExpansion.get();
        }

        @Override
        public void setObject(Set<BuildDocument> object) {

        }

        @Override
        public void detach() {

        }
    }

    private static class BuildsProvider implements ITreeProvider<BuildDocument> {

        final IModel<BuildFilter> filter;

        @SpringBean
        BuildRepository buildRepository;

        BuildsProvider(IModel<BuildFilter> filter) {
            this.filter = filter;
            Injector.get().inject(this);
        }

        @Override
        public void detach() {
            filter.detach();
        }

        @Override
        public Iterator<? extends BuildDocument> getRoots() {
            return buildRepository.getRoots(filter.getObject());
        }

        @Override
        public boolean hasChildren(BuildDocument node) {
            if (node.getJobName().contains("/")) {
                return node.getNumberOfChildren() != null && node.getNumberOfChildren() > 1;
            }
            return node.getNumberOfChildren() > 0;
        }

        @Override
        public Iterator<? extends BuildDocument> getChildren(BuildDocument node) {
            return buildRepository.getChildren(node, filter.getObject());
        }

        @Override
        public IModel<BuildDocument> model(BuildDocument object) {
            return new DocumentModel<>(object);
        }
    }
}