<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <h2>🎮 게임칩 등록</h2>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <v-text-field v-model="title" label="제목" />
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <v-text-field v-model.number="price" label="가격" type="number" />
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <v-textarea v-model="description" label="설명" auto-grow />
      </v-col>
    </v-row>

    <!-- 기존 썸네일 이미지 보여주기 -->
    <v-row v-if="thumbnailUrl" class="mb-2">
      <v-col cols="12">
        <h4>현재 썸네일</h4>
        <v-img :src="thumbnailUrl" height="150" contain />
        <v-btn color="error" small @click="removeThumbnail">썸네일 삭제</v-btn>
      </v-col>
    </v-row>

    <!-- 썸네일 업로드 -->
    <v-row>
      <v-col cols="12">
        <v-file-input
            label="썸네일 이미지 업로드 (1개)"
            prepend-icon="mdi-camera"
            show-size
            accept="image/*"
            :value="thumbnailFile ? [thumbnailFile] : []"
            @change="onThumbnailChange"
            clearable
        />
      </v-col>
    </v-row>

    <!-- 추가 이미지 업로드 -->
    <v-row>
      <v-col cols="12">
        <v-file-input
            label="추가 이미지 업로드 (여러 개 가능)"
            multiple
            prepend-icon="mdi-camera"
            show-size
            accept="image/*"
            v-model="addImageInputValue"
            @change="onAddImageFiles"
            hint="추가 선택 시 기존 파일에 추가됩니다."
            persistent-hint
            clearable
        />
      </v-col>
    </v-row>

    <!-- 추가 이미지 리스트 -->
    <v-row v-if="imageFiles.length > 0" class="mb-4">
      <v-col cols="12">
        <h4>선택한 추가 이미지 목록</h4>
        <v-chip-group column>
          <v-chip
              v-for="(file, index) in imageFiles"
              :key="file.name + '-' + index"
              class="ma-1"
              close
              @click:close="removeImage(index)"
          >
            {{ file.name }}
          </v-chip>
        </v-chip-group>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" class="text-right">
        <v-btn
            :style="primaryBtnStyle"
            class="btn-primary"
            @mouseover="hoverPrimary = true"
            @mouseleave="hoverPrimary = false"
            @click="onSubmit"
        >
          등록 완료
        </v-btn>

        <v-btn
            :style="errorBtnStyle"
            class="btn-error ml-2"
            @mouseover="hoverError = true"
            @mouseleave="hoverError = false"
            @click="onCancel"
        >
          취소
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useGameChipStore } from '../../stores/gameChipStore'

const title = ref('')
const description = ref('')
const price = ref(0)

// 썸네일 관련
const thumbnailFile = ref<File | null>(null)
const thumbnailUrl = ref('')

// 추가 이미지들 누적 배열
const imageFiles = ref<File[]>([])

// 추가 이미지 input v-model 값 (초기화용)
const addImageInputValue = ref<File[] | null>(null)

const hoverPrimary = ref(false)
const hoverError = ref(false)

const router = useRouter()
const gameChipStore = useGameChipStore()

const primaryBtnStyle = computed(() => ({
  backgroundColor: hoverPrimary.value ? '#1565c0' : '#1976d2',
  color: 'white',
  transition: 'background-color 0.3s ease',
}))

const errorBtnStyle = computed(() => ({
  backgroundColor: hoverError.value ? '#b71c1c' : '#d32f2f',
  color: 'white',
  transition: 'background-color 0.3s ease',
}))

// 썸네일 변경 이벤트 (Event 객체에서 파일 추출)
function onThumbnailChange(event: Event) {
  const target = event.target as HTMLInputElement
  const files = target.files
  if (!files || files.length === 0) {
    thumbnailFile.value = null
    thumbnailUrl.value = ''
    return
  }
  const file = files[0]
  thumbnailFile.value = file
  thumbnailUrl.value = URL.createObjectURL(file)
}

function removeThumbnail() {
  thumbnailFile.value = null
  thumbnailUrl.value = ''
}

// 추가 이미지 여러 개 누적 추가 처리 (Event 객체에서 파일 배열 추출)
function onAddImageFiles(event: Event) {
  const target = event.target as HTMLInputElement
  const files = target.files
  if (!files) return

  const filesArray = Array.from(files)

  // 중복 파일명 제거
  const filteredNewFiles = filesArray.filter(
      (f) => !imageFiles.value.some((existing) => existing.name === f.name)
  )

  imageFiles.value = [...imageFiles.value, ...filteredNewFiles]

  // input 값을 초기화해서 다음 선택시 change 이벤트가 잘 발생하게 한다
  addImageInputValue.value = null
}

function removeImage(index: number) {
  imageFiles.value.splice(index, 1)
}

const onSubmit = async () => {
  const priceNum = Number(price.value)

  if (
      !title.value.trim() ||
      !description.value.trim() ||
      isNaN(priceNum) ||
      priceNum <= 0 ||
      (!thumbnailFile.value && !thumbnailUrl.value)
  ) {
    alert('제목, 설명, 가격(0 초과), 썸네일은 필수입니다.')
    return
  }

  const formData = new FormData()
  formData.append('title', title.value)
  formData.append('description', description.value)
  formData.append('price', priceNum.toString())

  if (thumbnailFile.value) {
    formData.append('thumbnailFile', thumbnailFile.value)
  } else if (thumbnailUrl.value) {
    formData.append('thumbnailUrl', thumbnailUrl.value)
  }

  imageFiles.value.forEach((file) => {
    formData.append('imageFileList', file)
  })

  try {
    await gameChipStore.requestCreateGameChipToSpring(formData)
    alert('등록 완료!')
    router.push({ name: 'VueGameChipList' })
  } catch (error) {
    alert('등록 실패!')
    console.error(error)
  }
}

const onCancel = () => {
  router.go(-1)
}
</script>
