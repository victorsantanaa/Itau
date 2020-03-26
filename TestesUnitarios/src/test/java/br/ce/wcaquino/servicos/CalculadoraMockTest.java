package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.entidades.Locacao;

public class CalculadoraMockTest {
	
	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void devoMostrarADiferencaEntreMockESpy() {
		Mockito.when(calcMock.somar(1, 2)).thenReturn(5);
		//Mockito.when(calcSpy.somar(1, 2)).thenReturn(5);
		Mockito.doReturn(5).when(calcSpy).somar(1, 2);
		Mockito.doNothing().when(calcSpy).imprime();
		
		System.out.println("Mock: " + calcMock.somar(1, 2));
		System.out.println("Spy: " + calcSpy.somar(1, 2));
		
		System.out.println("Mock: ");
		calcMock.imprime();
		System.out.println("Spy: ");
		calcSpy.imprime();
		
	}
	
	
	
	
	
	@Test
	public void test() {
	Calculadora calc = Mockito.mock(Calculadora.class);
	
	ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
	Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);
	
	

	System.out.println(calc.somar(1, 9));
	System.out.println(argCapt.getAllValues());
	System.out.println(argCapt.getValue());
	
	}
	

}
