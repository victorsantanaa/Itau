package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiEmUmaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.builder.FilmeBuilder;
import br.ce.wcaquino.builder.LocacaoBuilder;
import br.ce.wcaquino.builder.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;



public class LocacaoDois {
	
	@InjectMocks @Spy
	private LocacaoService service;
	
	@Mock
	private SPCService spc;
	
	@Mock
	private LocacaoDAO dao;
	
	@Mock
	private EmailService email;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		//cenario
		
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		Usuario usuario = umUsuario().agora();
		
		Mockito.doReturn(DataUtils.obterData(28, 04, 2017)).when(service).obterData();
		
		//acao 
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao

		Assert.assertTrue((DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 04, 2017))));
		Assert.assertTrue((DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 04, 2017))));
	
	}


	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		//cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().semEstoque().agora());
		Usuario usuario = umUsuario().agora();
		
		//ação
		service.alugarFilme(usuario, filmes);
		System.out.println("Forma Elegante");
		
	}
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		try {
				service.alugarFilme(null, filmes);
				Assert.fail();
			} catch (LocadoraException e) {
				Assert.assertEquals(e.getMessage(), "Usuario vazio");
			}
		//	System.out.println("Forma Robusta");
			

		
	}
	
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = umUsuario().agora();
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		//ação
		service.alugarFilme(usuario, null);
		
		
	}
	
	@Test
	public void testLocacao_deveDarVinteECincoDeDescontoNoTerceiroFilme() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0), 
				new Filme("Filme 2", 1, 4.0), new Filme("Filme 3", 1, 4.0));
		Usuario usuario = umUsuario().agora();
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		//verificação
		Assert.assertEquals(Double.valueOf(11.0), resultado.getValor());
	}
	
	@Test
	public void testLocacao_deveDarCinquentaDeDescontoNoQuartoFilme() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0), 
				new Filme("Filme 2", 1, 4.0), new Filme("Filme 3", 1, 4.0), new Filme("Filme 4", 1, 4.0));
		Usuario usuario = umUsuario().agora();
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		//verificação
		Assert.assertEquals(Double.valueOf(13.0), resultado.getValor());
	}
	
	@Test
	public void testLocacao_deveDarSetentaECincoDeDescontoNoQuintoFilme() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0), 
				new Filme("Filme 2", 1, 4.0), new Filme("Filme 3", 1, 4.0), new Filme("Filme 4", 1, 4.0), new Filme("Filme 5", 1, 4.0));
		Usuario usuario = umUsuario().agora();
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		//verificação
		Assert.assertEquals(Double.valueOf(14.0), resultado.getValor());
	}
	
	@Test
	public void testLocacao_deveDarCemDeDescontoNoSextoFilme() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0), 
				new Filme("Filme 2", 1, 4.0), new Filme("Filme 3", 1, 4.0), new Filme("Filme 4", 1, 4.0), 
				new Filme("Filme 5", 1, 4.0), new Filme("Filme 6", 1, 4.0));
		Usuario usuario = umUsuario().agora();
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		//verificação
		Assert.assertEquals(Double.valueOf(14.0), resultado.getValor());
	}
	
	@Test
	public void testNaoDeveDevolverFilmenoDomingo() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		Mockito.doReturn(DataUtils.obterData(29, 04, 2017)).when(service).obterData();
		
		
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		assertThat(retorno.getDataRetorno(),caiEmUmaSegunda());
	}
	
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);
		
		//exception.expectMessage("Usuario Negativado");
		
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertEquals(e.getMessage(), "Usuario Negativado");
		}
		
		verify(spc).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro usuario Atrasado").agora();
		
		List<Locacao> locacoes = 
				Arrays.asList(
						umLocacao()
						.atrasado()
						.comUsuario(usuario)
						.agora(),
						umLocacao().comUsuario(usuario2).agora(),
						umLocacao().atrasado().comUsuario(usuario3).agora(),
						umLocacao().atrasado().comUsuario(usuario3).agora());
		
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.notificarAtrasos();
		
		//verificacao
		verify(email, times(3)).notificarAtraso(Mockito.any(Usuario.class));
		verify(email).notificarAtraso(usuario);
		verify(email, atLeastOnce()).notificarAtraso(usuario3);
		verify(email, never()).notificarAtraso(usuario2);
		verifyNoMoreInteractions(email);
		
	}
	
	@Test
	public void deveTratarErroNoSPC() throws Exception {
		//cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora(), FilmeBuilder.umFilme().agora());
		
		
		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha Catastrófica"));
		
		
		//verificação
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC, tente novamente");
		
		//ação
		service.alugarFilme(usuario, filmes);
		
		
		
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
		
		//cenario
		Locacao locacao = LocacaoBuilder.umLocacao().comDataRetorno(DataUtils.obterDataComDiferencaDias(0)).agora();
		
		
		//acao
		service.prorrogarLocacao(locacao, 3);
		
		//verificacao
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();
		
		
		
		Assert.assertEquals(Double.valueOf(12.0), locacaoRetornada.getValor());
		error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
		
	}
	

	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//acao
		Class<LocacaoService> clazz = LocacaoService.class;
		Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		metodo.setAccessible(true);
		
		
		
		
		Double valor = (Double) metodo.invoke(service, filmes);
		
		//verificacao
		Assert.assertEquals(Double.valueOf(4.0), valor);
		
	}

}


