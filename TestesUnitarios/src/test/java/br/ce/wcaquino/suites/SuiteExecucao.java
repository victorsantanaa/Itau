package br.ce.wcaquino.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalculadoraMockTest;
import br.ce.wcaquino.servicos.CalculadoraTEst;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoDois;

@RunWith(Suite.class)
@SuiteClasses({
	//CalculadoraTEst.class,
	CalculoValorLocacaoTest.class,
	LocacaoDois.class,
	//CalculadoraMockTest.class
	
})
public class SuiteExecucao {
	//Remova se puder
	
}
