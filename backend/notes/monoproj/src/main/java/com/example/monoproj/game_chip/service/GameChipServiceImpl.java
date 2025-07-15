package com.example.monoproj.game_chip.service;

import com.example.monoproj.account.entity.Account;
import com.example.monoproj.account.repository.AccountRepository;
import com.example.monoproj.game_chip.controller.response_form.ListGameChipResponseForm;
import com.example.monoproj.game_chip.entity.GameChip;
import com.example.monoproj.game_chip.entity.GameChipImage;
import com.example.monoproj.game_chip.entity.GameChipImageType;
import com.example.monoproj.game_chip.repository.GameChipImageRepository;
import com.example.monoproj.game_chip.repository.GameChipRepository;
import com.example.monoproj.game_chip.service.request.ListGameChipRequest;
import com.example.monoproj.game_chip.service.request.RegisterGameChipImageRequest;
import com.example.monoproj.game_chip.service.request.RegisterGameChipRequest;
import com.example.monoproj.game_chip.service.response.ListGameChipResponse;
import com.example.monoproj.game_chip.service.response.ReadGameChipResponse;
import com.example.monoproj.game_chip.service.response.RegisterGameChipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameChipServiceImpl implements GameChipService {

    private final GameChipImageRepository gameChipImageRepository;
    private final GameChipRepository gameChipRepository;
    private final AccountRepository accountRepository;

    @Override
    public RegisterGameChipResponse createGameChip(RegisterGameChipRequest gameChipRequest, RegisterGameChipImageRequest gameChipImageRequest) throws IOException {
        Long requestedAccountId = gameChipRequest.getAccountId();
        Account account = accountRepository.findById(requestedAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Account 존재하지 않음"));

        GameChip requestedGameChip = gameChipRequest.toGameChip(account);
        GameChip savedGameChip = gameChipRepository.save(requestedGameChip);

        GameChipImage requestedGameChipThumbnail = gameChipImageRequest.toGameChipThumbnail(savedGameChip);
        GameChipImage savedGameChipThumbnailImage = gameChipImageRepository.save(requestedGameChipThumbnail);

        List<GameChipImage> detailImageList = gameChipImageRequest.toGameChipImageList(savedGameChip);
        List<GameChipImage> savedGameChipDetailImageList = gameChipImageRepository.saveAll(detailImageList);

        List<GameChipImage> allImages = new ArrayList<>();
        allImages.add(savedGameChipThumbnailImage);
        allImages.addAll(savedGameChipDetailImageList);

        return RegisterGameChipResponse.from(savedGameChip, allImages);
    }

    @Override
    public ListGameChipResponse getAllGameChips(ListGameChipRequest request) {
        int page = request.getPage() - 1;
        int perPage = request.getPerPage();

        PageRequest pageRequest = PageRequest.of(page, perPage);
        Page<GameChip> pageResult = gameChipRepository.findAll(pageRequest);

        List<GameChip> gameChips = pageResult.getContent();

        List<GameChipImage> thumbnailImages = gameChipImageRepository.findAllByGameChipInAndType(gameChips, GameChipImageType.THUMBNAIL);
        Map<Long, byte[]> thumbnailMap = thumbnailImages.stream()
                .collect(Collectors.toMap(
                        img -> img.getGameChip().getId(),
                        GameChipImage::getImageData
                ));

        return ListGameChipResponse.from(
                pageResult.getContent(),
                pageResult.getNumber() + 1,   // 1-based current page
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                thumbnailMap
        );
    }

    @Override
    public ReadGameChipResponse readGameChip(Long gameChipId) {
        GameChip gameChip = gameChipRepository.findById(gameChipId)
                .orElseThrow(() -> new RuntimeException("GameChip 존재하지 않음 id: " + gameChipId));

        List<GameChipImage> images = gameChipImageRepository.findAllByGameChipId(gameChipId);

        return ReadGameChipResponse.from(gameChip, images);
    }

    @Override
    public void deleteGameChip(Long gameChipId, Long accountId) {
        GameChip chip = gameChipRepository.findById(gameChipId)
                .orElseThrow(() -> new RuntimeException("GameChip not found"));

        if (!chip.getAccount().getId().equals(accountId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        gameChipRepository.delete(chip);
    }

}
