package dk.smartcyp.core;

import java.util.HashMap;

import dk.smartcyp.smirks.SMARTCYPReaction;

/**

Matches atoms against SMARTS patterns and assigns predefined energies
Input is 3 columns 1) SMARTS pattern and 2) Energy 3) SMIRKS 
<pre>
[CX4;!CH0]	29.7 
[CX3;!CH0]	13.3
[cX3;!CH0]	0.7
[N]			999.9
[S]			44.5
</pre>
 */
public class SMARTSnEnergiesTable {

	// Local Variables
	private static HashMap<String, SMARTSData> SMARTSnEnergiesTable;		// Lookup table in which the SMARTS are key values to retrieve Energies



	protected SMARTSnEnergiesTable() {

		// Local Variable
		SMARTSnEnergiesTable = new HashMap<String, SMARTSData>();
		
		SMARTSnEnergiesTable.put("[SX2H1]", new SMARTSData(41.5, SMARTCYPReaction.new1));
		SMARTSnEnergiesTable.put("[$([SX2H0]);!$([S][*^2]);!$([S][CX4H0])]", 
								new SMARTSData(26.3, SMARTCYPReaction.new1) );
		SMARTSnEnergiesTable.put("[$([SX2H0][*^2]);!$([S](~[^2])[^2]);!$([S][CX4H0])]", 
								new SMARTSData(34.4, SMARTCYPReaction.new1) );
		SMARTSnEnergiesTable.put("[$([S][*D4H0]);$([SX2H0])]", 
								new SMARTSData(44.4, SMARTCYPReaction.new1) );
		SMARTSnEnergiesTable.put("[$([SX2H0]([*^2])[*^2]);!$([S][CX4H0])]", 
								new SMARTSData(46.9, SMARTCYPReaction.new1) );
		SMARTSnEnergiesTable.put("[sX2r5]", 
								new SMARTSData(70.0, SMARTCYPReaction.new1) );
		SMARTSnEnergiesTable.put("[$([#16X3](=[OX1]));$([#16]);!$([#16X3](=[OX1])[#6^2](~[#7^2]))]", 
								new SMARTSData(30.4, SMARTCYPReaction.new1) );
		SMARTSnEnergiesTable.put("[$([#16X3](=[OX1]));$([#16]);$([#16X3](=[OX1])[#6^2](~[#7^2]))]", 
								new SMARTSData(46.9, SMARTCYPReaction.new1));
		SMARTSnEnergiesTable.put("[$([CX3H1](=O)[#6])]", 
								new SMARTSData(40.2, SMARTCYPReaction.Aldehyde_oxidation) );
		SMARTSnEnergiesTable.put("[$([PX4]);$([P]=[S])]", 
								new SMARTSData(13.3, SMARTCYPReaction.Desulphurization_of_phosphor));
		SMARTSnEnergiesTable.put("[$([CX4]([#6^2])([#6^2])[#6^2]);!$([CH0])]", 
								new SMARTSData(33.1, SMARTCYPReaction.Aliphatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([CX4][N]);!$([CH0]);!$([C][N]([*^2])[*^2]);!$([C][N]=[#6X3]);!$([CX4][NX3][C]=[O]);!$([CX4][NX3][#16X4](=[OX1])(=[OX1]));!$([CX4][NX3][N]=[O]);!$([CX4]1[C][C]2[C][C][N]1[C][C]2)]", 
								new SMARTSData(39.8, SMARTCYPReaction.N_dealkylation) );
		SMARTSnEnergiesTable.put("[!$([CH0]);$([CX4]([C^2]~[C])[C^2]~[C]),$([CX4][#7]=[#6X3])]", 
								new SMARTSData(48.5, SMARTCYPReaction.Aliphatic_hydroxylation));
		SMARTSnEnergiesTable.put("[!$([CH0]);$([CX4]([#8])[#8]);!$([CX4]([#8])[#8][C]=[O])]", 
								new SMARTSData(48.5, SMARTCYPReaction.new3) );
		SMARTSnEnergiesTable.put("[$([CX4][S]);!$([CH0]);!$([C][S]=[O])]", 
								new SMARTSData(57.7, SMARTCYPReaction.Aliphatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([CX4][#6^2]~[#8]),$([CX4][cr5]),$([CX4]([c])[c]),$([CX4][#6^1]),$([CX4][C^2]=[C^2]-[#6^2]),$([CX4][NX3][N]=[O]);!$([CH0]);!$([CX4][C](=[O])[NX3]);!$([CX4][#6^2](=[#8])-[#8]);!$([CX4][C^2]([C^2])=[C^2]-[#6^2]);!$([CX4][#6^2](=[#8])[#6^2])]", 
								new SMARTSData(59.7, SMARTCYPReaction.Aliphatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([CX4][OH0]);!$([CH0]);!$([C][O][C]=[O]);!$([CX4]1[O][C]1)]", 
								new SMARTSData(62.2, SMARTCYPReaction.Aliphatic_hydroxylation));
		SMARTSnEnergiesTable.put("[$([CX4][OH1]);!$([CH0])]", 
								new SMARTSData(62.2, SMARTCYPReaction.Alcohol_oxidation) );
		SMARTSnEnergiesTable.put("[$([CX4][NX3H1][C]=[O]),$([CX4][#7](~[*^2])~[*^2]);!$([CH0]);!$([CX4][NX3H0][C]=[O])]", 
								new SMARTSData(63.9, SMARTCYPReaction.N_dealkylation));
		SMARTSnEnergiesTable.put("[$([CX4][#6^2]);!$([CH0]);!$([CX4][C](=[O])[NX3])]", 
								new SMARTSData(66.4, SMARTCYPReaction.Aliphatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([CX4][S](=[O])=[O]),$([CX4][NX3][#16X4](=[OX1])(=[OX1])),$([CX4][#6^2](=[#8])[#6^2]);!$([CH0])]", 
								new SMARTSData(72.3, SMARTCYPReaction.Aliphatic_hydroxylation));
		SMARTSnEnergiesTable.put("[$([CX4][NX3H2][#16X4](=[OX1])(=[OX1]));!$([CH0])]", 
								new SMARTSData(72.3, SMARTCYPReaction.N_dealkylation));
		SMARTSnEnergiesTable.put("[CX4;CH1,CH2;!$([CH3][NX3,C^2]);!$([CH3][OX2][C^2]=[O])]", //[CX4;CH1,CH2;!$([CH3][NX3,C^2]);!$([CH3][OX2][C^2][=O])]"
								new SMARTSData(76.1, SMARTCYPReaction.Aliphatic_hydroxylation));
		SMARTSnEnergiesTable.put("[CX4;$([CX4][OX2][C^2]=[O]);!$([CH0])]", //[CX4;$([CX4][OX2][C^2][=O]);!$([CH0])]
								new SMARTSData(76.1, SMARTCYPReaction.new2) );
		SMARTSnEnergiesTable.put("[CX4;$([CH3][NX3])]", 
								new SMARTSData(76.1, SMARTCYPReaction.N_dealkylation) );
		SMARTSnEnergiesTable.put("[CX4H3]", 
								new SMARTSData( 89.6, SMARTCYPReaction.Aliphatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([CX3H2]);$([C]=[*^2]-[*^2])]", 
								new SMARTSData(40.1, SMARTCYPReaction.Epoxidation) );
		SMARTSnEnergiesTable.put("[$([CX3H1]);$([C]=[*^2]-[*^2]);!$([C](-[*^2,a])=[*^2]-[*^2])]", 
								new SMARTSData(52.4, SMARTCYPReaction.Epoxidation) );
		SMARTSnEnergiesTable.put("[$([ch1r5]:[#8]),$([ch1r5](:[c]):[nH1])]", 
								new SMARTSData(52.9, SMARTCYPReaction.Epoxidation) );
		SMARTSnEnergiesTable.put("[$([ch1r5](:[nX3]):[nX2])]", 
								new SMARTSData(57.9, SMARTCYPReaction.Aromatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([ch1]1:[c](-[N^3]([C^3])[C^3]):[c]:[c]:[c]:[c]1),$([ch1]1:[c]:[c]:[c](-[N^3]([C^3])[C^3]):[c]:[c]1),$([ch1]1:[c](-[N^3H1][C^3]):[c]:[c]:[c]:[c]1),$([ch1]1:[c]:[c]:[c](-[N^3H1][C^3]):[c]:[c]1),$([ch1]1:[c](-[N^3H2]):[c]:[c]:[c]:[c]1),$([ch1]1:[c]:[c]:[c](-[N^3H2]):[c]:[c]1)]", 
								new SMARTSData(59.5, SMARTCYPReaction.Aromatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([CX3]);$([CX3]=[CX3]);!$([CH0]);!$([CX3](-[*^2])=[CX3]);!$([CX3]=[CX3]-[*^2])]", 
								new SMARTSData(65.6, SMARTCYPReaction.Epoxidation) );
		SMARTSnEnergiesTable.put("[$([ch1]1:[c]:[c]:[cr6]([#7]~[#6^2]):[c]:[c]1),$([ch1r6]:[cr5]:[nX3r5]);!$([c]1:[c]:[c]:[c](-[N]-[C]=[O]):[c]:[c]1)]", 
								new SMARTSData(68.2, SMARTCYPReaction.Aromatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([ch1r5]:[#16X2]:[c]),$([ch1r5]:[c]:[nX3])]", 
								new SMARTSData(69.4, SMARTCYPReaction.Aromatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([ch1]);!$([c]1:[c]:[c]:[c](-[O]-[C^2]~[O]):[c]:[c]1);$([c]1:[c]:[c]:[c](-[NH]-[C]=[O]):[c]:[c]1),$([c]1:[c]:[c]:[c](-[O,SX2]):[c]:[c]1),$([ch1r6]:[cr6]:[cr5]:[nX3r5]),$([ch1r6]:[cr5]:[cr5]:[nX3r5,oX2r5,sX2r5])]", 
								new SMARTSData(74.1, SMARTCYPReaction.Aromatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([ch1]);$([ch1]1:[c](~[#7X2]~[#6^2]):[c]:[c]:[c]:[c]1),$([ch1r6]:[cr6]-[O,SX2]),$([ch1r6]:[cr5]:[sX2r5]),$([ch1r6]:[cr6]:[cr5]:[or5,sX2r5]),$([ch1r6]:[cr6]:[cr6]:[cr5]:[nX3r5]);!$([ch1r6]:[cr6]-[O]-[C^2]~[O])]", 
								new SMARTSData(77.2, SMARTCYPReaction.Aromatic_hydroxylation));
		SMARTSnEnergiesTable.put("[$([ch1r5]);!$([ch1r5]:[nX2]:[#16X2])]", 
								new SMARTSData(78.1, SMARTCYPReaction.Aromatic_hydroxylation) );
		 
		SMARTSnEnergiesTable.put("[$([ch1]1:[c]([#6^2,#6^1,O]):[c]:[c]:[c]:[c]1),$([ch1]1:[c]:[c]:[c]([#6^2,#6^1,O]):[c]:[c]1),$([ch1]1:[c]([NX3](~[O])~[O]):[c]:[c]:[c]:[c]1),$([ch1]1:[c]:[c]:[c]([NX3](~[O])~[O]):[c]:[c]1),$([ch1r6]:[cr5]:[or5]),$([ch1r6]:[cr6]:[cr6]:[cr5]:[or5,sX2r5])]", 
								new SMARTSData(80.8, SMARTCYPReaction.Aromatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([C^2h1](-[C^2])=[C^2])]", 
								new SMARTSData(80.8, SMARTCYPReaction.Epoxidation) );
		SMARTSnEnergiesTable.put("[$([ch1]1:[c]([F,Cl,I,Br]):[c]:[c]:[c]:[c]1),$([ch1]1:[c]:[c]:[c]([F,Cl,I,Br]):[c]:[c]1),$([ch1]1:[c]:[c]([NX3](~[O])~[O]):[c]:[c]:[c]1),$([ch1]1:[c]:[c]:[c]([S]=O):[c]:[c]1)]", 
								new SMARTSData(84.1, SMARTCYPReaction.Aromatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([#6^2h1]);!$([ch1r6]:[nr6]);!$([ch1]:[c]-[S](=O)(=O)-[NX3])]", 
								new SMARTSData(86.3, SMARTCYPReaction.Aromatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([ch1r6]:[nr6]),$([ch1]:[c]-[S](=O)(=O)-[NX3])]", 
								new SMARTSData(92.0, SMARTCYPReaction.Aromatic_hydroxylation) );
		SMARTSnEnergiesTable.put("[$([N^3H0]);!$([N^3][*^2]);!$([NX3][#16X4](=[OX1])(=[OX1]))]", 
								new SMARTSData(41.0, SMARTCYPReaction.N_oxidation) );
		SMARTSnEnergiesTable.put("[$([N^3]);$([H1,H2]);!$([NX3][#16X4](=[OX1])(=[OX1]))]", 
								new SMARTSData(54.1, SMARTCYPReaction.new4) );
		SMARTSnEnergiesTable.put("[$([NX3H0]([#6^2]1)[#6^2]=[#6^2][#6^3][#6^2]=1)]", 
								new SMARTSData( 61.9, SMARTCYPReaction.N_oxidation) );
		SMARTSnEnergiesTable.put("[$([N]([C^3])=[C^2])]", 
								new SMARTSData(62.7, SMARTCYPReaction.N_oxidation));
		SMARTSnEnergiesTable.put("[$([N^3H0]);$([N^3][*^2]);!$([N^3]([*^2])[*^2]);!$([NX3][#16X4](=[OX1])(=[OX1]))]", 
								new SMARTSData(63.9, SMARTCYPReaction.N_oxidation));
		SMARTSnEnergiesTable.put("[$([nr6]),$([N](-[#6^2])=[#6^2]);!$([nr5H0])]", 
								new SMARTSData(75.6, SMARTCYPReaction.N_oxidation) );
		SMARTSnEnergiesTable.put("[$([N]);$([NX3H0]([*^2])[*^2]),$([N^2][C]=[O])]", 
								new SMARTSData(91.1, SMARTCYPReaction.N_oxidation) );
		SMARTSnEnergiesTable.put("[nr5H0]", 
								new SMARTSData(92.1, SMARTCYPReaction.N_oxidation) );
		SMARTSnEnergiesTable.put("[$([NX3]);$([NX3][#16X4](=[OX1])(=[OX1]))]", 
								new SMARTSData(94.4, SMARTCYPReaction.new4) );
		SMARTSnEnergiesTable.put("[$([NX3H1]([#6^2]1)[#6^2]=[#6^2][#6^3][#6^2]=1)]", 
								new SMARTSData(10.4, SMARTCYPReaction.Aromatization_of_dihydropyridines) );
	}




	public static synchronized HashMap<String, SMARTSData> getSMARTSnEnergiesTable(){
		if (SMARTSnEnergiesTable==null) new SMARTSnEnergiesTable();
		return SMARTSnEnergiesTable;
	}

}