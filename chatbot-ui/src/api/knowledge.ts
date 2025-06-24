import request from '@/utils/request'
import type { KnowledgeBase, PageResponse } from './types'

export const knowledgeApi = {
    getKnowledgeList: (page: number, size: number) => {
        return request.get<PageResponse<KnowledgeBase>>('/api/knowledge', {
            params: { page, size }
        })
    },

    searchKnowledge: (keyword: string, page: number, size: number) => {
        return request.get<PageResponse<KnowledgeBase>>('/api/knowledge/search', {
            params: { keyword, page, size }
        })
    },

    findByCategory: (category: string, page: number, size: number) => {
        return request.get<PageResponse<KnowledgeBase>>(`/api/knowledge/category/${category}`, {
            params: { page, size }
        })
    },

    addKnowledge: (knowledge: KnowledgeBase) => {
        return request.post<KnowledgeBase>('/api/knowledge', knowledge)
    },

    updateKnowledge: (knowledge: KnowledgeBase) => {
        return request.put<KnowledgeBase>(`/api/knowledge/${knowledge.id}`, knowledge)
    },

    deleteKnowledge: (id: number) => {
        return request.delete(`/api/knowledge/${id}`)
    },

    batchImportKnowledge: (knowledgeList: KnowledgeBase[]) => {
        return request.post('/api/knowledge/batch-import', knowledgeList)
    },

    /**
     * 下载Excel文件（BIO方式）
     */
    downloadExcelBio: () => {
        return request.get('/api/knowledge/export/bio', {
            responseType: 'blob'
        })
    },

    /**
     * 下载Excel文件（NIO方式）
     */
    downloadExcelNio: () => {
        return request.get('/api/knowledge/export/nio', {
            responseType: 'blob'
        })
    },

    /**
     * 下载Excel文件（流式NIO方式）
     */
    downloadExcelStreamingNio: () => {
        return request.get('/api/knowledge/export/streaming-nio', {
            responseType: 'blob'
        })
    },

    /**
     * 下载CSV文件（流式导出）
     */
    downloadCsv: () => {
        return request.get('/api/knowledge/export/csv', {
            responseType: 'blob'
        })
    }
}; 