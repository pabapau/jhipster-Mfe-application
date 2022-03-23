package com.paba.mfe.service.impl;

import com.paba.mfe.domain.FileDescriptor;
import com.paba.mfe.repository.FileDescriptorRepository;
import com.paba.mfe.service.FileDescriptorService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FileDescriptor}.
 */
@Service
@Transactional
public class FileDescriptorServiceImpl implements FileDescriptorService {

    private final Logger log = LoggerFactory.getLogger(FileDescriptorServiceImpl.class);

    private final FileDescriptorRepository fileDescriptorRepository;

    public FileDescriptorServiceImpl(FileDescriptorRepository fileDescriptorRepository) {
        this.fileDescriptorRepository = fileDescriptorRepository;
    }

    @Override
    public FileDescriptor save(FileDescriptor fileDescriptor) {
        log.debug("Request to save FileDescriptor : {}", fileDescriptor);
        return fileDescriptorRepository.save(fileDescriptor);
    }

    @Override
    public Optional<FileDescriptor> partialUpdate(FileDescriptor fileDescriptor) {
        log.debug("Request to partially update FileDescriptor : {}", fileDescriptor);

        return fileDescriptorRepository
            .findById(fileDescriptor.getId())
            .map(existingFileDescriptor -> {
                if (fileDescriptor.getFileIdent() != null) {
                    existingFileDescriptor.setFileIdent(fileDescriptor.getFileIdent());
                }
                if (fileDescriptor.getFlowUseCase() != null) {
                    existingFileDescriptor.setFlowUseCase(fileDescriptor.getFlowUseCase());
                }
                if (fileDescriptor.getDescription() != null) {
                    existingFileDescriptor.setDescription(fileDescriptor.getDescription());
                }
                if (fileDescriptor.getCreationDate() != null) {
                    existingFileDescriptor.setCreationDate(fileDescriptor.getCreationDate());
                }
                if (fileDescriptor.getLastUpdated() != null) {
                    existingFileDescriptor.setLastUpdated(fileDescriptor.getLastUpdated());
                }
                if (fileDescriptor.getBuildState() != null) {
                    existingFileDescriptor.setBuildState(fileDescriptor.getBuildState());
                }
                if (fileDescriptor.getBuildCount() != null) {
                    existingFileDescriptor.setBuildCount(fileDescriptor.getBuildCount());
                }
                if (fileDescriptor.getBuildComment() != null) {
                    existingFileDescriptor.setBuildComment(fileDescriptor.getBuildComment());
                }

                return existingFileDescriptor;
            })
            .map(fileDescriptorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileDescriptor> findAll(Pageable pageable) {
        log.debug("Request to get all FileDescriptors");
        return fileDescriptorRepository.findAll(pageable);
    }

    /**
     *  Get all the fileDescriptors where IsSourceFor is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FileDescriptor> findAllWhereIsSourceForIsNull() {
        log.debug("Request to get all fileDescriptors where IsSourceFor is null");
        return StreamSupport
            .stream(fileDescriptorRepository.findAll().spliterator(), false)
            .filter(fileDescriptor -> fileDescriptor.getIsSourceFor() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get all the fileDescriptors where IsDestFor is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FileDescriptor> findAllWhereIsDestForIsNull() {
        log.debug("Request to get all fileDescriptors where IsDestFor is null");
        return StreamSupport
            .stream(fileDescriptorRepository.findAll().spliterator(), false)
            .filter(fileDescriptor -> fileDescriptor.getIsDestFor() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileDescriptor> findOne(Long id) {
        log.debug("Request to get FileDescriptor : {}", id);
        return fileDescriptorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileDescriptor : {}", id);
        fileDescriptorRepository.deleteById(id);
    }
}
