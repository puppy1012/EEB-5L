import axiosInstance from "../utility/axiosInstance.ts";

export const gameChipActions = {
    async requestGameChipListToSpring(page: number, perPage: number): Promise<void> {
        try {
            const res = await axiosInstance.springAxiosInst.get('/game-chip/list', {
                params: { page, perPage }
            })
            this.gameChipList = res.data.gameChipList
            console.log('res.data.gameChipList:', res.data.gameChipList)
        } catch (error) {
            console.error('requestGameChipListToSpring():', error)
            throw error
        }
    },

    // async requestGameChipToSpring(id: number): Promise<void> {
    //     try {
    //         const res = await axiosInstance.springAxiosInst.get(`/game-chip/read/${id}`)
    //         this.gameChip = res.data
    //     } catch (error) {
    //         alert('requestGameChipToSpring() 문제 발생!')
    //         throw error
    //     }
    // },

    async requestCreateGameChipToSpring(payload: {
        title: string
        content: string
    }): Promise<any> {  // 반환 타입은 보통 서버 응답에 따라 적절히 수정하세요
        try {
            const res = await axiosInstance.springAxiosInst.post('/game-chip/register', payload)
            alert('등록 성공!')
            return res.data  // 보통 등록 후 생성된 게시물 정보를 반환하면 편리합니다.
        } catch (error) {
            alert('requestCreateBoardToSpring() 문제 발생')
            throw error
        }
    },

    // async requestDeleteGameChipToSpring(boardId: number): Promise<void> {
    //     try {
    //         await axiosInstance.springAxiosInst.delete(`/game-chip/delete/${boardId}`)
    //         alert('삭제 성공!')
    //     } catch (error) {
    //         alert('requestDeleteBoardToSpring() 문제 발생')
    //         throw error
    //     }
    // },
    //
    // async requestUpdateGameChipToSpring(payload: {
    //     boardId: number
    //     title: string
    //     content: string
    //     writer: string
    // }): Promise<void> {
    //     try {
    //         await axiosInstance.springAxiosInst.put(`/game-chip/update/${payload.boardId}`, payload)
    //         alert('수정 성공!')
    //     } catch (error) {
    //         alert('requestUpdateBoardToSpring() 문제 발생')
    //         throw error
    //     }
    // },
}
