package com.example.englishlerningapp.flashcard;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlashcardService {
    private final FlashcardRepository flashcardRepository;
    private final FlashcardMapper flashcardMapper;

    public FlashcardService(FlashcardRepository flashcardRepository, FlashcardMapper flashcardMapper) {
        this.flashcardRepository = flashcardRepository;
        this.flashcardMapper = flashcardMapper;
    }

    FlashcardDto saveFlashcard(FlashcardDto dto) {
        Flashcard flashcardToSave = flashcardMapper.map(dto);
        Flashcard savedFlashcard = flashcardRepository.save(flashcardToSave);

        return flashcardMapper.map(savedFlashcard);
    }

    Optional<FlashcardDto> replaceFlashcard(Long id, FlashcardDto flashcardDto) {
        if (!flashcardRepository.existsById(id)){
            return Optional.empty();
        }

        flashcardDto.setId(id);
        Flashcard flashcardToReplace = flashcardMapper.map(flashcardDto);
        Flashcard replacedFlashcard = flashcardRepository.save(flashcardToReplace);

        return Optional.of(flashcardMapper.map(replacedFlashcard));
    }

    void deleteFlashcard(Long id) {
        flashcardRepository.deleteById(id);
    }

    Optional<FlashcardDto> updateFlashcard(FlashcardDto flashcardDto) {
        if(!flashcardRepository.existsById(flashcardDto.getId())) {
            return Optional.empty();
        }

        Flashcard flashcard = flashcardRepository.findById(flashcardDto.getId()).get();
        Flashcard flashcardToUpdate = setEntityFields(flashcardDto, flashcard);
        Flashcard updatedFlashcard = flashcardRepository.save(flashcardToUpdate);

        return Optional.of(flashcardMapper.map(updatedFlashcard));
    }

    private Flashcard setEntityFields(FlashcardDto source, Flashcard target) {
        if (source.getPolishesWord() != null) {
            target.setPolishesWord(source.getPolishesWord());
        }
        if (source.getEnglishesWord() != null) {
            target.setEnglishesWord(source.getEnglishesWord());
        }
        if (source.getGermansWord() != null) {
            target.setGermansWord(source.getGermansWord());
        }

        return target;
    }

    Optional<List<FlashcardDto>> takeAllFlashcards() {
        List<FlashcardDto> flashcardDtoList = new ArrayList<>();

        flashcardRepository.findAll().forEach(flashcard ->
                flashcardDtoList.add(flashcardMapper.map(flashcard)));

        return Optional.of(flashcardDtoList);
    }

    Optional<FlashcardDto> takeFlashcard(Long id){
        if (!flashcardRepository.existsById(id)) {
            return Optional.empty();
        }

        Flashcard flashcard = flashcardRepository.findById(id).get();

        return Optional.of(flashcardMapper.map(flashcard));
    }
}
