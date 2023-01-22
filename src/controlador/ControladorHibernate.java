/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.util.ArrayList;
import java.util.Date;
import modelo.Departamentos;
import modelo.Empleados;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.HibernateUtil;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Usuario
 */
public class ControladorHibernate {

    public ControladorHibernate() {
    }

    public List lanzarConsulta(String consulta) {
        //Inicio la variable que recogera los resultados
        List resultList = null;
        //Inicio la sesion con HibernateUtil. No va en try-catch porque HibernateUtil ya lanza excepcion si hay fallo.
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        //Capturamos toda excepcion que pueda ocurrir al lanzar una consulta
        try {
        
                Query query = sesion.createQuery(consulta);
//                query.setFirstResult(2);  
//                query.setMaxResults(3); 
                resultList = (List) query.list();
                 
                sesion.getTransaction();
         
        } catch (Exception e) {
            String errQuery = "Ha ocurrido un error inesperado lanzando la consulta";
            System.err.println(errQuery);
            JOptionPane.showMessageDialog(null, errQuery, "ERROR", 0);
        } finally {
            //Cerramos la sesion
            sesion.close();
        }
        return resultList;
    }
    
    public boolean InsertarEmpleados(Empleados c) {
        boolean result = false;
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        try {

            sesion.save(c);
            sesion.getTransaction().commit();
            result = true;

        } catch (org.hibernate.exception.ConstraintViolationException e) {
            sesion.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "Error al insertar empleado, la clave esta duplicada", "Error", 0);
        } finally {
            sesion.close();
        }
        return result;
    }

    public boolean InsertarDepartamentos(Departamentos d) {
        boolean result = false;
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.beginTransaction();
        try {

            sesion.save(d);
            sesion.getTransaction().commit();
            result = true;

        } catch (org.hibernate.exception.ConstraintViolationException e) {
            sesion.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "Error al insertar departamento, la clave esta duplicada", "Error", 0);
        } finally {
            sesion.close();
        }
        return result;
    }

    public boolean EliminarEmpleados(Short empNo) {
        //Inicio la variable que recogera los resultados
        boolean ejecutada = false;
        Empleados emp = new Empleados();
        //Inicio la sesion con HibernateUtil. No va en try-catch porque HibernateUtil ya lanza excepcion si hay fallo.

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        emp = (Empleados) session.load(Empleados.class, empNo);
        //Capturamos toda excepcion que pueda ocurrir al lanzar una consulta
        try {

            session.delete(emp);
            ejecutada = true;
            tx.commit();
            System.out.println("Ejecución");

        } catch (Exception e) {
            String errQuery = "Ha ocurrido un error inesperado en la eliminación";
            System.err.println(errQuery);
            JOptionPane.showMessageDialog(null, errQuery, "ERROR", 0);
        } finally {
            //Cerramos la sesion

            session.close();
        }
        return ejecutada;
    }

    public boolean actualizar(short numero, String Apellido, String Oficio, float comision, float salario, short dir, Date fecha, Departamentos dep) {
        //Inicio la variable que recogera los resultados
        boolean ejecutada = false;
        Empleados e = new Empleados();
        //Inicio la sesion con HibernateUtil. No va en try-catch porque HibernateUtil ya lanza excepcion si hay fallo.

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        e = (Empleados) session.load(Empleados.class, numero);

        //Capturamos toda excepcion que pueda ocurrir al lanzar una consulta
        try {
            if (session != null) {

                e.setApellido(Apellido);
                e.setOficio(Oficio);
                e.setComision(comision);
                e.setSalario(salario);
                e.setDir(dir);
                e.setFechaAlt(fecha);
                e.setDepartamentos(dep);

                session.update(e);
                ejecutada = true;
                tx.commit();
                System.out.println("Ejecución");
            }

        } catch (ConstraintViolationException he) {
            //Deshacemos los cambios en caso de error
            tx.rollback();
            JOptionPane.showMessageDialog(null, "Error al modificar departamento\n" + he.getLocalizedMessage());
        } catch (ObjectNotFoundException he) {
            //Deshacemos los cambios en caso de error
            tx.rollback();
            JOptionPane.showMessageDialog(null, "El empleado no existe\n" + he.getLocalizedMessage());
        } catch (Exception ex) {
            String errQuery = "Ha ocurrido un error inesperado en la actualización";
            System.err.println(errQuery);
            JOptionPane.showMessageDialog(null, errQuery, "ERROR", 0);
        } finally {
            //Cerramos la sesion

            session.close();
        }
        return ejecutada;
    }
    
    public float maximo(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("SELECT MAX(e.salario) FROM Empleados e");
        Float salariomax = (Float) query.uniqueResult();
        transaction.commit();
        session.close();
        return salariomax;        
    }
    
    public float minimo(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("SELECT MIN(e.salario) FROM Empleados e");
        Float salariomax = (Float) query.uniqueResult();
        transaction.commit();
        session.close();
        return salariomax;        
    }
}
