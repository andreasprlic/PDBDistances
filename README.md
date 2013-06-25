PDBDistances
============

Find proteins by the distances between their atoms.

### How It's Done ###

*Note: Queries have an origin and target; the usage of the word 'origin' and 'target' refer to the atoms described in a distance query.*

Each group gets its own file of precalculated data. These files are in fact trees; the levels of the tree are, starting at the highest level of the tree,

 * Origin group (ASP, TYR, AU, etc.) (These are BioJava groups; nucleotides or ligands)
 * Origin element (O, C, N, Pb, Uuu, etc.)
 * Origin atom name (N1, CA', O3, etc.)
 * Target group
 * Target element
 * Target atom name
 * Result datum

The result data contain information the following information about a match:

 * PDB ID of the protein that had that match.
 * Origin atom information
 * Target atom information
 * The distance between origin and target

Atom information contains the following information to uniquely and quickly find a particular atom in the database:

 * PDB ID
 * Model number
 * Chain ID
 * Group name
 * Residue number
 * Atom serial number

PDB ID and group name are both already stored in this tree of data; they aren't redundantly stored but are both returned when a search gives back results.
