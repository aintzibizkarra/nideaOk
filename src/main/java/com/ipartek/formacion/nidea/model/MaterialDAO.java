package com.ipartek.formacion.nidea.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ipartek.formacion.nidea.pojo.Material;

public class MaterialDAO implements Persistible<Material> {

	private static MaterialDAO INSTANCE = null;

	// Private constructor NO se pueda hacer new y crear N instacias (objetos)
	private MaterialDAO() {
	}

	// creador synchronized para protegerse de posibles problemas multi-hilo
	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MaterialDAO();
		}
	}

	// otra prueba para evitar instanciación múltiple
	public static MaterialDAO getInstance() {
		if (INSTANCE == null) {
			createInstance();
		}
		return INSTANCE;
	}

	/**
	 * Recupera todos los materiales de la BBDD ordenados por id descendente
	 * 
	 * @return ArrayList<Material> si no existen registros new ArrayList<Material>()
	 */
	public ArrayList<Material> getAll() {

		ArrayList<Material> lista = new ArrayList<Material>();
		String sql = "SELECT id, nombre, precio FROM material ORDER BY id DESC LIMIT 500;";

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery();) {
			Material m = null;
			while (rs.next()) {
				m = mapper(rs);
				lista.add(m);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lista;
	}

	public ArrayList<Material> getByName(String search) {

		ArrayList<Material> lista = new ArrayList<Material>();
		String sql = "SELECT id, nombre, precio FROM material WHERE nombre LIKE ? ORDER BY id DESC LIMIT 500;";

		try (Connection con = ConnectionManager.getConnection(); PreparedStatement pst = con.prepareStatement(sql);) {
			pst.setString(1, "%" + search + "%");
			try (ResultSet rs = pst.executeQuery()) {
				Material m = null;
				while (rs.next()) {
					m = mapper(rs);
					lista.add(m);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lista;
	}

	@Override
	public Material getById(int id) {
		Material material = null;
		String sql = "SELECT `id`, `nombre`, `precio` FROM `material` WHERE `id`= ?;";

		try (Connection con = ConnectionManager.getConnection(); PreparedStatement pst = con.prepareStatement(sql);) {
			pst.setInt(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					material = mapper(rs);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return material;
	}

	@Override
	public boolean save(Material pojo) {
		boolean resul = false;

		if (pojo != null) {
			try {
				if (pojo.getId() == -1) {
					resul = crear(pojo);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		} else {
			try {
				resul = modificar(pojo);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return resul;

	}

	private boolean crear(Material pojo) throws Exception {
		boolean resultado = false;
		String sql = "INSERT INTO `material` (`nombre`, `precio`) VALUES (?, ?); ";
		// PreparedStatement.RETURN_GENERATED_KEYS me devuelve las claves generadas. El
		// prepareStatement permite un segundo parametro
		try (Connection con = ConnectionManager.getConnection(); PreparedStatement pst = con.prepareStatement(sql);) {

			pst.setString(1, pojo.getNombre().trim());
			pst.setFloat(2, pojo.getPrecio());

			int affetedRows = pst.executeUpdate();

			if (affetedRows == 1) {

				// Recuperar ID generado de forma automatica
				try (ResultSet rs = pst.getGeneratedKeys();) {
					while (rs.next()) {
						pojo.setId(rs.getInt(1));
						resultado = true;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultado;
	}

	private boolean modificar(Material pojo) throws Exception {
		boolean resul = false;
		String sql = "UPDATE `material` SET `nombre`= ? , `precio`= ? WHERE  `id`= ?;";
		try (Connection con = ConnectionManager.getConnection(); PreparedStatement pst = con.prepareStatement(sql);) {

			pst.setString(1, pojo.getNombre().trim());
			pst.setFloat(2, pojo.getPrecio());
			pst.setInt(3, pojo.getId());

			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				resul = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resul;
	}

	@Override
	public boolean delete(int id) {
		boolean resul = false;
		String sql = "DELETE FROM `material` WHERE  `id`= ?;";

		try (Connection con = ConnectionManager.getConnection(); PreparedStatement pst = con.prepareStatement(sql);) {
			pst.setInt(1, id);

			int affetedRows = pst.executeUpdate();

			if (affetedRows == 1) {
				resul = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resul;

	}

	@Override
	public Material mapper(ResultSet rs) throws SQLException {
		Material m = null;
		if (rs != null) {
			m = new Material();
			m.setNombre(rs.getString("nombre"));
			m.setId(rs.getInt("id"));
			m.setPrecio(rs.getFloat("precio"));
		}

		return m;
	}

}
