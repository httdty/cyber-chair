package userauth.utility;

import userauth.domain.PCMemberRelation;
import userauth.utility.contract.PCmemberRelationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class ApiUtil {

    @Autowired
    private RestTemplate restTemplate;

    public String encodeUriForGet(MultiValueMap<String, String> params, String uri) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        return builder.queryParams(params).build().encode().toString();
    }

    public ResponseEntity<List<PCMemberRelation>> findPcmemberByPcmemberIdAndStatus(
            MultiValueMap<String, String> params, String uri, HttpEntity<MultiValueMap<String, String>> entity){
        return restTemplate.exchange(
                encodeUriForGet(params, uri),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<PCMemberRelation>>() {
                });
    }
}
