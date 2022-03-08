package patrick.com.springpadraoprojeto.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import patrick.com.springpadraoprojeto.spring.model.Aluno;
import patrick.com.springpadraoprojeto.spring.model.AlunoRepository;
import patrick.com.springpadraoprojeto.spring.model.Endereco;
import patrick.com.springpadraoprojeto.spring.model.EnderecoRepository;
import patrick.com.springpadraoprojeto.spring.service.AlunoService;
import patrick.com.springpadraoprojeto.spring.service.ViaCepService;

import java.util.Optional;


@Service
public class AlunoServiceImpl implements AlunoService {


    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;


    @Override
    public Iterable<Aluno> buscarTodos() {

        return alunoRepository.findAll();
    }

    @Override
    public Aluno buscarPorId(Long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        return aluno.get();
    }

    @Override
    public void inserir(Aluno aluno) {
        salvarAlunoComCep(aluno);
    }

    @Override
    public void atualizar(Long id, Aluno aluno) {

        Optional<Aluno> alunoBd = alunoRepository.findById(id);
        if (alunoBd.isPresent()) {
            salvarAlunoComCep(aluno);
        }
    }

    @Override
    public void deletar(Long id) {
        alunoRepository.deleteById(id);
    }

    private void salvarAlunoComCep(Aluno aluno) {
        String cep = aluno.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        aluno.setEndereco(endereco);

        alunoRepository.save(aluno);
    }

}
