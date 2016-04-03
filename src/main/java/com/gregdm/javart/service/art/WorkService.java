package com.gregdm.javart.service.art;

import com.gregdm.javart.domain.art.Work;
import com.gregdm.javart.repository.art.WorkRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Created by Greg on 26/03/2016.
 */
@Service
public class WorkService {

    @Inject
    private WorkRepository workRepository;

    public Optional<Work> get(String id){
        return workRepository.get(id);
    }
    public List<Work> search(String search){
        return workRepository.search(search);
    }

}
