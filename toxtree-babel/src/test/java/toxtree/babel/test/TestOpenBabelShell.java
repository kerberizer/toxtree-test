package toxtree.babel.test;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.interfaces.IMolecule;

import ambit2.base.external.CommandShell;
import ambit2.core.smiles.OpenBabelShell;


public class TestOpenBabelShell {

	@Test
	public void test() throws Exception {
		String osName = System.getProperty("os.name");
		if (CommandShell.os_WINDOWS.equals(osName) || CommandShell.os_WINDOWSVISTA.equals(osName)){
			OpenBabelShell ob = new OpenBabelShell();
			IMolecule mol = ob.process("c12-c3c(cccc3)Nc1nc(N)cc2");
			Assert.assertTrue(mol.getAtomCount()>0);
		}
	}
}