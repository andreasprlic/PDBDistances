package com.ulyssecarion.pdb.morgan.graph;

import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.structure.Element;

import chemaxon.struc.MolAtom;

/**
 * This class takes care of implementing a basic {@link Atom} and {@link Bond}
 * class for graphs, and provides PDB- and ChemAxon-specific versions of the
 * {@link Atom} class (namely {@link PDBAtom} and {@link ChemAxonAtom}).
 * 
 * @author Ulysse Carion
 */
public class GraphDriver {
	/**
	 * An atom stores its element and the bonds its in.
	 * 
	 * @see PDBAtom which also stores an atom name
	 * @author Ulysse Carion
	 */
	public static class Atom {
		private Element element;
		private List<Bond> bonds;

		private int currentConnectivity;
		private int nextConnectivity;

		/**
		 * Builds an atom with a given {@link Element}. Note that connectivity
		 * values are not yet initialized after calling this method; you must
		 * call {@link #initializeConnectivity()} to do so.
		 * 
		 * @param element
		 */
		public Atom(Element element) {
			this.element = element;
			bonds = new ArrayList<Bond>();
			currentConnectivity = -1;
			nextConnectivity = -1;
		}

		/**
		 * What Element is this atom of?
		 * 
		 * @return this atom's element
		 */
		public Element getElement() {
			return element;
		}

		/**
		 * What bonds is this atom in?
		 * 
		 * @return this atom's bond list.
		 */
		public List<Bond> getBonds() {
			return bonds;
		}

		@Override
		public String toString() {
			String s = element + " -> [ ";
			for (Bond b : bonds) {
				s += b.getOther(this).element + "-" + b.getOrder() + " ";
			}
			return s + "] (" + getHashValue() + ")";
		}

		/**
		 * Initializes this atom's connectivity using the
		 * {@link #getHashValue()} method.
		 */
		public void initializeConnectivity() {
			nextConnectivity = getHashValue();
		}

		/**
		 * Calculates the next value of connectivity for an iteration of
		 * Morgan's algorithm. The next value is reflected in the value of
		 * {@link #getNextConnectivity()}.
		 */
		public void prepareNextConnectivity() {
			int nextVal = 0;

			for (Bond bond : bonds) {
				nextVal += bond.getOther(this).currentConnectivity;
			}

			nextConnectivity = nextVal + currentConnectivity;
		}

		/**
		 * Changes this atom's current connectivity to its future connectivity.
		 */
		public void useNextConnectivity() {
			currentConnectivity = nextConnectivity;
			nextConnectivity = -1;
		}

		/**
		 * Gets this atom's current connectivity
		 * 
		 * @return this atoms's current connectivity
		 */
		public int getCurrentConnectivity() {
			return currentConnectivity;
		}

		/**
		 * Gets this atom's next connectivity
		 * 
		 * @return this atom's next connectivity
		 */
		public int getNextConnectivity() {
			return nextConnectivity;
		}

		/**
		 * An atom's initial (aka "hash") value is calculated as:
		 * 
		 * <pre>
		 * Initial Value = Atomic Number * 10 + Pi-Centeredness
		 * </pre>
		 * 
		 * This method calculates such a hash value.
		 * 
		 * @see #isPiCenter() for determining pi-centeredness
		 * @return the initial value of this atom to run Morgan's algorithm with
		 */
		public int getHashValue() {
			int atomicNumberScore = 10 * element.getAtomicNumber();
			int piCenterScore = isPiCenter() ? 1 : 0;

			return atomicNumberScore + piCenterScore;
		}

		/**
		 * Determines if this atom is in a pi center. This method returns true
		 * if:
		 * 
		 * <ul>
		 * <li>It forms a bond with another atom, and that bond has an order
		 * greater than 1, OR</li>
		 * <li>It is electronegative (more on this later), and is single-bonded
		 * to an atom that is in turn double-bonded to an electronegative atom.</li>
		 * </ul>
		 * 
		 * We determine an atom to be electronegative if its valence electron
		 * count is between 5 and 7, inclusive.
		 * 
		 * @return true if this atom is in a pi center, false otherwise
		 */
		public boolean isPiCenter() {
			return hasHighOrderBond() || isInInterchangeableBond();
		}

		/**
		 * Returns true if this atom is in a bond that has an order greater than
		 * one.
		 * 
		 * @return true if this atom is in a higher-order bond, false otherwise
		 */
		private boolean hasHighOrderBond() {
			for (Bond bond : bonds) {
				if (bond.getOrder() > 1) {
					return true;
				}
			}

			return false;
		}

		/**
		 * Returns true if all the following are true:
		 * <ol>
		 * <li>This atom is electronegative.</li>
		 * <li>This atom is single bonded to some other atom <i>A</li> and
		 * <i>A</i> is in turn double-bonded to some electronegative atom.
		 * </ol>
		 * 
		 * @return true if this atom is in some kind of bond whose bond order is
		 *         not really '1' but closer to '1.5' because it's shared with
		 *         another atom.
		 */
		private boolean isInInterchangeableBond() {
			if (!isElectronegative()) {
				return false;
			}

			for (Bond bond : bonds) {
				if (bond.getOrder() == 1) {
					Atom candidateCenter = bond.getOther(this);
					for (Bond centerBond : candidateCenter.bonds) {
						if (centerBond.getOrder() == 2) {
							Atom candidateAlternate = centerBond
									.getOther(candidateCenter);

							if (candidateAlternate.isElectronegative()) {
								return true;
							}
						}
					}
				}
			}

			return false;
		}

		/**
		 * Returns true if this atom's element's valence electron count is
		 * between 5 and 7, inclusive.
		 * 
		 * @return true if this atom is electronegative, false otherwise
		 */
		public boolean isElectronegative() {
			int valence = element.getValenceElectronCount();

			return valence >= 5 && valence <= 7;
		}
	}

	/**
	 * Like a regular Atom, but it also has an atom name.
	 * 
	 * @author Ulysse Carion
	 */
	public static class PDBAtom extends Atom {
		private String atomName;

		public PDBAtom(Element element, String atomName) {
			super(element);
			this.atomName = atomName;
		}

		public String getAtomName() {
			return atomName;
		}

		@Override
		public String toString() {
			String s = getElement() + " (" + atomName + ") -> [ ";
			for (Bond b : getBonds()) {
				s += b.getOther(this).element + "-" + b.getOrder() + " ";
			}
			return s + "] (" + getHashValue() + ")";
		}
	}

	/**
	 * Like a regular atom, but but it also has a {@link MolAtom} counterpart.
	 * 
	 * @author Ulysse Carion
	 */
	public static class ChemAxonAtom extends Atom {
		private MolAtom molAtom;

		public ChemAxonAtom(Element element, MolAtom molAtom) {
			super(element);
			this.molAtom = molAtom;
		}

		public MolAtom getMolAtom() {
			return molAtom;
		}
	}

	/**
	 * Keeps track of two atoms and the number of electrons they covalently
	 * share. Remember to call {@link Bond#addSelfToAtoms()} after creating one
	 * of these.
	 * 
	 * @author Ulysse Carion
	 */
	public static class Bond {
		private Atom a;
		private Atom b;
		private int order;

		public Bond(Atom a, Atom b, int order) {
			this.a = a;
			this.b = b;
			this.order = order;
		}

		public Atom getOther(Atom exclude) {
			return exclude == a ? b : a;
		}

		public int getOrder() {
			return order;
		}

		public void addSelfToAtoms() {
			a.getBonds().add(this);
			b.getBonds().add(this);
		}
	}
}
