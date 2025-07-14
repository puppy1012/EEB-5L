package com.example.monoproj.game_chip.service;

import com.example.monoproj.game_chip.service.request.ListGameChipRequest;
import com.example.monoproj.game_chip.service.request.RegisterGameChipImageRequest;
import com.example.monoproj.game_chip.service.request.RegisterGameChipRequest;
import com.example.monoproj.game_chip.service.response.ListGameChipResponse;
import com.example.monoproj.game_chip.service.response.ReadGameChipResponse;
import com.example.monoproj.game_chip.service.response.RegisterGameChipResponse;

import java.io.IOException;

public interface GameChipService {
    RegisterGameChipResponse createGameChip(RegisterGameChipRequest gameChipRequest, RegisterGameChipImageRequest gameChipImageRequest) throws IOException;
    ListGameChipResponse getAllGameChips(ListGameChipRequest request);
    ReadGameChipResponse readGameChip(Long gameChipId);
}
