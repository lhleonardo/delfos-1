package br.com.delfos.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewFactory<Type> {
	public TableView<Type> criaTableView(List<Type> itens) {
		Field[] fields = itens.get(0).getClass().getDeclaredFields();
		List<TableColumn<Type, ?>> columns = getColumns(fields);

		TableView<Type> tabela = new TableView<>();
		columns.forEach(column -> tabela.getColumns().add(column));
		tabela.setItems(FXCollections.observableArrayList(itens));

		tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		return tabela;
	}

	private List<TableColumn<Type, ?>> getColumns(Field[] fields) {
		List<TableColumn<Type, ?>> columns = new ArrayList<>();
		for (Field f : fields) {
			if (f.isAnnotationPresent(br.com.delfos.view.Column.class)) {

				br.com.delfos.view.Column annotation = f.getAnnotation(br.com.delfos.view.Column.class);
				TableColumn<Type, ?> tableColumn = criaColuna(f.getType(), annotation.alias(),
				        annotation.name());

				columns.add(tableColumn);
			}
		}
		return columns;
	}

	private TableColumn<Type, ?> criaColuna(Class<?> type, String alias, String name) {
		TableColumn<Type, ?> column;
		if (type == String.class) {
			column = new TableColumn<Type, String>(alias);
		} else if (type == Long.class) {
			column = new TableColumn<Type, Long>(alias);
		} else if (type == Integer.class) {
			column = new TableColumn<Type, Integer>(alias);
		} else if (type == Boolean.class) {
			column = new TableColumn<Type, Object>(alias);
		} else if (type == Double.class) {
			column = new TableColumn<Type, Object>(alias);
		} else
			column = new TableColumn<Type, Object>(alias);

		configuraColuna(column, name);

		return column;
	}

	private void configuraColuna(TableColumn<Type, ?> column, String name) {
		column.setCellValueFactory(new PropertyValueFactory<>(name));
	}
}