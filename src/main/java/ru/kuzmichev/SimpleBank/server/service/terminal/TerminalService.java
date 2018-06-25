package ru.kuzmichev.SimpleBank.server.service.terminal;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.kuzmichev.SimpleBank.server.service.terminal.repository.TerminalEntity;
import ru.kuzmichev.SimpleBank.server.service.terminal.repository.TerminalRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kuzmichev.SimpleBank.server.util.Converters.convert;

@Slf4j
@Service
public class TerminalService {

    @Autowired
    private TerminalRepository terminalRepository;

    @Nullable
    @Transactional(readOnly = true)
    public Terminal getAvailableTerminalById(long id) {
        log.debug("Get available terminal with id [{}]", id);
        TerminalEntity terminalEntity;
        try {
            terminalEntity = terminalRepository.getOne(id);
        } catch (EntityNotFoundException e) {
            log.debug("Terminal not found [{}]", id);
            return null;
        }
        if (!terminalEntity.isEnable()) {
            return null;
        }
        return convert(terminalEntity);
    }

    @Transactional(readOnly = true)
    public List<Terminal> getAllByIds(List<Long> ids) {
        log.debug("Get all terminals with ids [{}]", ids);
        if (CollectionUtils.isEmpty(ids)) {
            return terminalRepository.findAll().stream()
                    .map(t -> convert(t))
                    .collect(Collectors.toList());
        }
        return terminalRepository.findAllById(ids).stream()
                .map(t -> convert(t))
                .collect(Collectors.toList());
    }
}
