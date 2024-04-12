package com.generation.crud_farmacia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.crud_farmacia.model.Produto;
import com.generation.crud_farmacia.repository.CategoriaRepository;
import com.generation.crud_farmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtosRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<List<Produto>> getAll() {

		return ResponseEntity.ok(produtosRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id) {

		return produtosRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
		if (categoriaRepository.existsById(produto.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(produtosRepository.save(produto));
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria inválida", null);
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> findByNome(@PathVariable String nome) {
		return ResponseEntity.ok(produtosRepository.findByNome(nome));
	}

	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
		if (produtosRepository.existsById(produto.getId())) {
			if (categoriaRepository.existsById(produto.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(produtosRepository.save(produto));

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria inválida", null);
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Produto> produto = produtosRepository.findById(id);

		if (produto.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		produtosRepository.deleteById(id);

	}

}
