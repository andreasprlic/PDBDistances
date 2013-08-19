package com.ulyssecarion.pdb.morgan.graph;

import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.structure.Element;

import chemaxon.formats.MolImporter;
import chemaxon.license.LicenseManager;
import chemaxon.license.LicenseProcessingException;
import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;
import chemaxon.struc.Molecule;

import com.ulyssecarion.pdb.morgan.graph.GraphDriver.Atom;
import com.ulyssecarion.pdb.morgan.graph.GraphDriver.Bond;
import com.ulyssecarion.pdb.morgan.graph.GraphDriver.ChemAxonAtom;

public class ChemAxonGraphMaker {
	public static List<ChemAxonAtom> getGraph(Molecule m) {
		try {
			LicenseManager
					.setLicenseFile("/Users/ulysse/Documents/workspace/pdbinabox/licences/chemaxon/license.cxl");
		} catch (LicenseProcessingException e) {
			e.printStackTrace();
		}

		List<ChemAxonAtom> atoms = new ArrayList<ChemAxonAtom>();
		List<MolAtom> molAtoms = new ArrayList<MolAtom>();

		for (MolAtom atom : m.getAtomArray()) {
			Element element = getElement(atom);

			if (element.isHeavyAtom()) {
				atoms.add(new ChemAxonAtom(element, atom));
				molAtoms.add(atom);
			}
		}

		for (MolBond bond : m.getBondArray()) {
			MolAtom molAtom1 = bond.getAtom1();
			MolAtom molAtom2 = bond.getAtom2();

			if (getElement(molAtom1).isHeavyAtom()
					&& getElement(molAtom2).isHeavyAtom()) {
				Atom atom1 = atoms.get(molAtoms.indexOf(molAtom1));
				Atom atom2 = atoms.get(molAtoms.indexOf(molAtom2));

				new Bond(atom1, atom2, bond.getType()).addSelfToAtoms();
			}
		}

		return atoms;
	}
	
	/**
	 * Gets a list of atoms with bonds from an inputted smiles string using the
	 * ChemAxon API.
	 * <p>
	 * Note: this method has some hardcoded aspects to it. First of all, it
	 * strictly uses SMILES strings, but this can easily be modified to use some
	 * other format (MRV, or plain old ChemAxon Molecule if that's what you
	 * have). Second, this method has hard-coded the location of the License
	 * file for ChemAxon. You might want to get rid of that line or modify it
	 * depending on where your license is / whether you've already set it or
	 * not.
	 * 
	 * @param smiles
	 *            the SMILES string of the molecule you want to import
	 * @return a list of atoms, with bonds already formed that constitute the
	 *         passed molecule.
	 */
	public static List<ChemAxonAtom> getGraph(String smiles) {
		try {
			Molecule m = MolImporter.importMol(smiles, "smiles");
			return getGraph(m);
		} catch (Exception e) {
			return null;
		}
	}

	private static Element getElement(MolAtom atom) {
		return Element.valueOfIgnoreCase(atom.getSymbol());
	}
}
