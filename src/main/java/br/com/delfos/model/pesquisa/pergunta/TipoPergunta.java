package br.com.delfos.model.pesquisa.pergunta;

import java.util.Arrays;
import java.util.List;

interface Conversor {
	Alternativa getType();

	String getLocation();

	String getLocationResposta();

	Pergunta<?> create(Alternativa alternativa);
}

public enum TipoPergunta implements Conversor {
	TEXTO {

		@Override
		public Alternativa getType() {
			return new Texto();
		}

		@Override
		public String getLocation() {
			return "fxml/pergunta/ConfigTextoParagrafoView.fxml";
		}

		@Override
		public Pergunta<?> create(Alternativa alternativa) {
			Pergunta<Texto> pergunta = new Pergunta<>();
			pergunta.setAlternativa((Texto) alternativa);

			return pergunta;
		}

		@Override
		public String getLocationResposta() {
			return "fxml/resposta/TemplateTexto.fxml";
		}

	},
	PARAGRAFO {
		@Override
		public Alternativa getType() {
			return new Paragrafo();
		}

		@Override
		public String getLocation() {
			return "fxml/pergunta/ConfigTextoParagrafoView.fxml";
		}

		@Override
		public Pergunta<?> create(Alternativa alternativa) {
			Pergunta<Paragrafo> paragrafo = new Pergunta<>();
			paragrafo.setAlternativa((Paragrafo) alternativa);

			return paragrafo;
		}

		@Override
		public String getLocationResposta() {
			return "fxml/resposta/TemplateParagrafo.fxml";
		}

	},
	MULTIPLA_ESCOLHA {

		@Override
		public Alternativa getType() {
			return new MultiplaEscolha();
		}

		@Override
		public String getLocation() {
			return "fxml/pergunta/ConfigMultiplaEscolhaView.fxml";
		}

		@Override
		public Pergunta<?> create(Alternativa alternativa) {
			Pergunta<MultiplaEscolha> m = new Pergunta<>();
			m.setAlternativa((MultiplaEscolha) alternativa);
			return m;
		}

		@Override
		public String getLocationResposta() {
			return "fxml/resposta/TemplateMultiplaEscolha.fxml";
		}

	},
	INTERVALO {

		@Override
		public Alternativa getType() {
			return new Intervalo();
		}

		@Override
		public String getLocation() {
			return "fxml/pergunta/ConfigIntervaloView.fxml";
		}

		@Override
		public Pergunta<?> create(Alternativa alternativa) {
			Pergunta<Intervalo> i = new Pergunta<>();
			i.setAlternativa((Intervalo) alternativa);
			return i;
		}

		@Override
		public String getLocationResposta() {
			return "fxml/resposta/TemplateIntervalo.fxml";
		}

	};

	public static List<TipoPergunta> getAll() {
		return Arrays.asList(TipoPergunta.values());
	}

}
