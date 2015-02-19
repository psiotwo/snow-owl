package org.protege.editor.owl.ui.editor;

import javax.swing.JComponent;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.selector.OWLAnnotationPropertySelectorPanel;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

/**
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Jun 4, 2009<br><br>
 */
public class OWLAnnotationPropertyEditor extends AbstractOWLObjectEditor<OWLAnnotationProperty> {

    private OWLAnnotationPropertySelectorPanel editor;


    public OWLAnnotationPropertyEditor(OWLEditorKit owlEditorKit) {
        editor = new OWLAnnotationPropertySelectorPanel(owlEditorKit);
    }


    public OWLAnnotationProperty getEditedObject() {
        return editor.getSelectedObject();
    }

    public boolean setEditedObject(OWLAnnotationProperty p){
        editor.setSelection(p);        
        return true;
    }


    public String getEditorTypeName() {
        return "Annotation Property";
    }


    public boolean canEdit(Object object) {
        return object instanceof OWLAnnotationProperty;
    }


    public JComponent getEditorComponent() {
        return editor;
    }


    public void dispose() {
        editor.dispose();
    }
}
