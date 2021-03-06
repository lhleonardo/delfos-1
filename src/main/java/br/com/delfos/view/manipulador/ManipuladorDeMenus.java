package br.com.delfos.view.manipulador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Set;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import br.com.delfos.model.auditoria.Funcionalidade;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class ManipuladorDeMenus {
	private InputStream file;
	private Set<Funcionalidade> permissoes;

	public ManipuladorDeMenus(FileInputStream file, Set<Funcionalidade> permissoes) {
		this.file = file;
		this.permissoes = permissoes;
	}

	public ManipuladorDeMenus(Set<Funcionalidade> permissoes) throws URISyntaxException, FileNotFoundException {
		this.permissoes = permissoes;

		this.file = ManipuladorDeMenus.class.getClassLoader().getResourceAsStream("config/menu.xml");

	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		System.out.println(ManipuladorDeMenus.class.getClassLoader().getResource("config/menu.xml").toURI());
	}

	public MenuBar create() throws JDOMException, IOException {
		Element rootElement = new SAXBuilder().build(file).getRootElement();

		MenuBar bar = new MenuBar();

		rootElement.getChildren().forEach(element -> {
			bar.getMenus().add(createMenu(element));
		});

		return bar;
	}

	private Menu createMenu(Element rootParentElement) {
		Menu menu = new Menu();
		menu.setText(rootParentElement.getChildText("name"));
		menu.setId(rootParentElement.getAttributeValue("id"));

		rootParentElement.getChildren().forEach(parentElement -> {
			if (parentElement.getName().equals("itens")) {
				parentElement.getChildren().forEach(element -> {
					if (element.getName().equals("menu")) {
						menu.getItems().add(createMenu(element));
					} else if (element.getName().equals("menuItem")) {
						menu.getItems().add(createMenuItem(element));
					}
				});
			}
		});

		menu.setVisible(configuraVisibilidade(menu.getId()));
		return menu;
	}

	private boolean configuraVisibilidade(String idDoMenu) {
		boolean result = false;

		if (idDoMenu.contains(":"))
			idDoMenu = idDoMenu.split(":")[0];

		for (Funcionalidade f : permissoes) {
			if (result)
				break;
			result = f.getChave().equals(idDoMenu);
		}

		return result;
	}

	private MenuItem createMenuItem(Element element) {
		MenuItem item = new MenuItem(element.getChildText("name"));
		item.setId(String.format("%s:%s%s", element.getAttributeValue("id"), element.getChildText("view"),
				element.getChildText("icon")));
		item.setVisible(configuraVisibilidade(item.getId()));
		return item;
	}
}
