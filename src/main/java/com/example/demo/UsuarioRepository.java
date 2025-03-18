package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario_bd, Long> {
    Usuario_bd findByNombreusuario(String nombreusuario);
}