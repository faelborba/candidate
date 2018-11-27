package br.edu.ulbra.election.candidate.client;

import br.edu.ulbra.election.candidate.output.v1.ResultOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class ResultClientService {

    private ResultClient resultClient;

    @Autowired
    public ResultClientService (ResultClient resultClient){
        this.resultClient = resultClient;
    }

    public ResultOutput getById(Long id){
        return this.resultClient.getById(id);
    }

    @FeignClient(value="result-service", url="${url.result-service}")
    private interface ResultClient {

        @GetMapping("/v1/result/candidate/{candidateId}")
        ResultOutput getById(@PathVariable(name = "candidateId") Long candidateId);
    }
}
