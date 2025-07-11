package com.hms2.repository.impl;

import com.hms2.model.Doctor;
import com.hms2.model.Department;
import com.hms2.repository.DoctorRepository;
import com.hms2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@ApplicationScoped
public class DoctorRepositoryImpl extends GenericRepositoryImpl<Doctor, Long>
        implements DoctorRepository {

    private static final Logger logger = LoggerFactory.getLogger(DoctorRepositoryImpl.class);

    @Override
    public List<Doctor> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Finding all doctors");
            return session.createQuery("FROM Doctor", Doctor.class).list();
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error finding all doctors: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Doctor> findDeletedDoctors() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Finding deleted doctors");
            return session.createQuery("FROM Doctor WHERE status = 'DELETED'", Doctor.class).list();
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error finding deleted doctors: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Doctor> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Finding doctor by ID: " + id);
            Doctor doctor = session.get(Doctor.class, id);
            return Optional.ofNullable(doctor);
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error finding doctor by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Doctor> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Finding doctor by email: " + email);
            Doctor doctor = session.createQuery("FROM Doctor WHERE email = :email", Doctor.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return Optional.ofNullable(doctor);
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error finding doctor by email: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Doctor> findByLicenseNumber(String licenseNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Finding doctor by license number: " + licenseNumber);
            Doctor doctor = session.createQuery("FROM Doctor WHERE licenseNumber = :licenseNumber", Doctor.class)
                    .setParameter("licenseNumber", licenseNumber)
                    .uniqueResult();
            return Optional.ofNullable(doctor);
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error finding doctor by license number: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Doctor> findBySpecialization(String specialization) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Finding doctors by specialization: " + specialization);
            return session.createQuery("FROM Doctor WHERE specialization = :specialization", Doctor.class)
                    .setParameter("specialization", specialization)
                    .list();
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error finding doctors by specialization: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Doctor> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Finding doctors by status: " + status);
            return session.createQuery("FROM Doctor WHERE status = :status", Doctor.class)
                    .setParameter("status", status)
                    .list();
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error finding doctors by status: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Doctor> findByActive(boolean active) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Finding doctors by active status: " + active);
            return session.createQuery("FROM Doctor WHERE active = :active", Doctor.class)
                    .setParameter("active", active)
                    .list();
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error finding doctors by active status: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Doctor> searchByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Searching doctors by name: " + name);
            return session.createQuery("FROM Doctor WHERE LOWER(firstName) LIKE LOWER(:name) OR LOWER(lastName) LIKE LOWER(:name)", Doctor.class)
                    .setParameter("name", "%" + name + "%")
                    .list();
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error searching doctors by name: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public long countBySpecialization(String specialization) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Counting doctors by specialization: " + specialization);
            return session.createQuery("SELECT COUNT(*) FROM Doctor WHERE specialization = :specialization", Long.class)
                    .setParameter("specialization", specialization)
                    .uniqueResult();
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error counting doctors by specialization: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public long countByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Counting doctors by status: " + status);
            return session.createQuery("SELECT COUNT(*) FROM Doctor WHERE status = :status", Long.class)
                    .setParameter("status", status)
                    .uniqueResult();
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error counting doctors by status: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Optional<Doctor> findByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.err.println("[DoctorRepositoryImpl] Finding doctor by user ID: " + userId);
            Doctor doctor = session.createQuery("FROM Doctor WHERE user.id = :userId", Doctor.class)
                    .setParameter("userId", userId)
                    .uniqueResult();
            return Optional.ofNullable(doctor);
        } catch (Exception e) {
            System.err.println("[DoctorRepositoryImpl] Error finding doctor by user ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void deleteDoctorsByIds(List<Long> doctorIds) {
        if (doctorIds != null) {
            for (Long id : doctorIds) {
                deleteById(id);
            }
        }
    }
}
