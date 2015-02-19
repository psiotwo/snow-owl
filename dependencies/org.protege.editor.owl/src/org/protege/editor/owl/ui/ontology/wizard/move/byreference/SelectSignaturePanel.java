package org.protege.editor.owl.ui.ontology.wizard.move.byreference;

import java.awt.BorderLayout;

import org.protege.editor.owl.ui.ontology.wizard.move.MoveAxiomsKitConfigurationPanel;
import org.protege.editor.owl.ui.selector.OWLEntitySelectorPanel;


/**
 * Author: Matthew Horridge<br> The University Of Manchester<br> Information Management Group<br> Date:
 * 23-Sep-2008<br><br>
 */
public class SelectSignaturePanel extends MoveAxiomsKitConfigurationPanel {

    private OWLEntitySelectorPanel selector;

    private MoveAxiomsByReferenceKit kit;


    public SelectSignaturePanel(MoveAxiomsByReferenceKit kit) {
        this.kit = kit;
    }


    public void initialise() {
        setLayout(new BorderLayout());
        selector = new OWLEntitySelectorPanel(getEditorKit());
        add(selector, BorderLayout.CENTER);
    }


    public void dispose() {
        selector.dispose();
    }


    public String getID() {
        return "Signature panel";
    }


    public String getTitle() {
        return "Select a signature";
    }


    public String getInstructions() {
        return "Select entities in that will be used as a signature for your extraction." +
               "\nAll axioms referencing entities in the signature will be selected by default.";
    }


    public void update() {
    }


    public void commit() {
        kit.setSignature(selector.getSelectedObjects());
    }
}
