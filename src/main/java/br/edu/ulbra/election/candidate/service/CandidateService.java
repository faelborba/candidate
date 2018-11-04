package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.exception.GenericOutputException;
import br.edu.ulbra.election.candidate.input.v1.CandidateInput;
import br.edu.ulbra.election.candidate.model.Candidate;
import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;
import br.edu.ulbra.election.candidate.output.v1.ElectionOutput;
import br.edu.ulbra.election.candidate.output.v1.GenericOutput;
import br.edu.ulbra.election.candidate.output.v1.PartyOutput;
import br.edu.ulbra.election.candidate.repository.CandidateRepository;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    private final ModelMapper modelMapper;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_CANDIDATE_NOT_FOUND = "Candidate not found";

    @Autowired
    public CandidateService(CandidateRepository candidateRepository, ModelMapper modelMapper){
        this.candidateRepository = candidateRepository;
        this.modelMapper = modelMapper;
    }

    public List<CandidateOutput> getAll(){
        /*Type candidateOutputListType = new TypeToken<List<CandidateOutput>>(){}.getType();*/
        List<Candidate> candidateList = (List<Candidate>) candidateRepository.findAll();
        List<CandidateOutput> candidateOutputList = new ArrayList<>();
        for(Candidate candidate : candidateList){
            CandidateOutput candidateOutput = modelMapper.map(candidate,CandidateOutput.class);
            ElectionOutput electionOutput = new ElectionOutput();
            electionOutput.setId(candidate.getElectionId());
            candidateOutput.setElectionOutput(electionOutput);
            PartyOutput partyOutput = new PartyOutput();
            partyOutput.setId(candidate.getPartyId());
            candidateOutput.setPartyOutput(partyOutput);

            candidateOutputList.add(candidateOutput);
        }
        /*return modelMapper.map(candidateRepository.findAll(), candidateOutputList);*/
        return candidateOutputList;
    }

    public CandidateOutput create(CandidateInput candidateInput){
        this.validateInput(candidateInput);
        Candidate candidate = modelMapper.map(candidateInput, Candidate.class);
        candidate = candidateRepository.save(candidate);
        CandidateOutput candidateOutput = modelMapper.map(candidate, CandidateOutput.class);

        ElectionOutput electionOutput = new ElectionOutput();
        electionOutput.setId(candidate.getElectionId());
        candidateOutput.setElectionOutput(electionOutput);

        PartyOutput partyOutput = new PartyOutput();
        partyOutput.setId(candidate.getPartyId());
        candidateOutput.setPartyOutput(partyOutput);

        return candidateOutput;
    }

    public CandidateOutput getById(Long candidateId){
        if(candidateId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null){
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

        CandidateOutput candidateOutput = modelMapper.map(candidate, CandidateOutput.class);

        ElectionOutput electionOutput = new ElectionOutput();
        electionOutput.setId(candidate.getElectionId());
        candidateOutput.setElectionOutput(electionOutput);

        PartyOutput partyOutput = new PartyOutput();
        partyOutput.setId(candidate.getPartyId());
        candidateOutput.setPartyOutput(partyOutput);

        return modelMapper.map(candidateOutput, CandidateOutput.class);
    }

    public CandidateOutput update(Long candidateId, CandidateInput candidateInput){
        if(candidateId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(candidateInput);

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if(candidate ==  null){
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

        candidate.setName(candidateInput.getName());
        candidate.setNumberElection(candidateInput.getNumberElection());
        candidate.setElectionId(candidateInput.getElectionId());
        candidate.setPartyId(candidateInput.getPartyId());
        candidate = candidateRepository.save(candidate);
        return modelMapper.map(candidate, CandidateOutput.class);
    }

    public GenericOutput delete(Long candidateId) {
        if (candidateId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null){
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

        candidateRepository.delete(candidate);

        return new GenericOutput("Voter deleted");
    }

    private void validateInput(CandidateInput candidateInput){

        if (StringUtils.isBlank(candidateInput.getName())){
            throw new GenericOutputException("Invalid name");
        }

        if (validateName(candidateInput.getName())){
            throw new GenericOutputException("Invalid name");
        }

        if (candidateInput.getPartyId() == null){
            throw new GenericOutputException("Invalid party ID");
        }

        if (candidateInput.getNumberElection() == null){
            throw new GenericOutputException("Invalid number election");
        }

        if (candidateInput.getElectionId() == null){
            throw new GenericOutputException("Invalid election ID");
        }
    }

    private boolean validateName(String name){
        if(name.split(" ").length == 1){// verificando se tem somente o primeiro nome
            System.out.println("Sem sobre nome");
            return true;
        }
        if(name.split(" ")[0].length() < 5){
            System.out.println("Sem tamanho");
            return true;
        }
        return false;
    }
}
