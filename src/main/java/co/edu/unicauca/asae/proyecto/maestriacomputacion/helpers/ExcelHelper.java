package co.edu.unicauca.asae.proyecto.maestriacomputacion.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import co.edu.unicauca.asae.proyecto.maestriacomputacion.entities.Estudiante;

public class ExcelHelper {
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "codigo", "identificacion", "tipoIdentificacion", "nombres", "apellidos", "correo",
			"correoInstitucional", "telefono", "genero", "direccion" };
	static String SHEET = "Estudiantes";

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<Estudiante> saveEstudiantes(InputStream is) {
		try {
			Workbook workbook = new XSSFWorkbook(is);

			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();

			List<Estudiante> estudiantes = new ArrayList<Estudiante>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				Estudiante estudiante = new Estudiante();
				String currentValue = "";
				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					if (currentCell.getCellType() == CellType.STRING) {
						currentValue = currentCell.getStringCellValue();
					}
					if (currentCell.getCellType() == CellType.NUMERIC) {
						currentValue = String.valueOf(currentCell.getNumericCellValue());
					}

					switch (cellIdx) {
					case 0:
						estudiante.setCodigo(currentValue);
						break;

					case 1:
						estudiante.setIdentificacion(Long.parseLong(currentValue));
						break;

					case 2:
						estudiante.setTipoIdentificacion((String) currentValue);
						break;

					case 3:
						estudiante.setNombres((String) currentValue);
						break;

					case 4:
						estudiante.setApellidos((String) currentValue);
						break;

					case 5:
						estudiante.setCorreo((String) currentValue);
						break;

					case 6:
						estudiante.setCorreoInstitucional((String) currentValue);
						break;

					case 7:
						estudiante.setTelefono((String) currentValue);
						break;

					case 8:
						estudiante.setGenero((String) currentValue);
						break;

					case 9:
						estudiante.setDireccion((String) currentValue);
						break;

					default:
						break;
					}

					cellIdx++;
				}

				estudiantes.add(estudiante);
			}

			workbook.close();

			return estudiantes;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
}
