import { saveAs } from 'file-saver'
import XLSX from 'xlsx'

export function export_table_to_excel(tableId, fileName, fileType) { // fixme: 导出卡号信息丢失
  fileName = fileName || '交易明细'
  fileType = fileType || 'xlsx'
  const tables = document.getElementById(tableId)
  const wb = XLSX.utils.table_to_book(tables)
  const wbOut = XLSX.write(wb, {
    bookType: fileType,
    bookSST: true,
    type: 'array'
  })
  try {
    saveAs(
      new Blob([wbOut], { type: 'application/octet-stream' }),
      fileName + '.' + fileType
    )
  } catch (e) {
    if (typeof console !== 'undefined') console.log(e, wbOut)
  }
  return wbOut
}

export function export_json_to_excel({ // fixme: 测试失败
  header,
  data,
  filename = '交易明细',
  bookType = 'xlsx'
} = {}) {
  const wb = {
    SheetNames: ['Sheet1'],
    Sheets: {},
    Props: {}
  }
  wb.Sheets['Sheet1'] = XLSX.utils.json_to_sheet([header, ...data], { skipHeader: true })

  const wbOut = XLSX.write(wb, {
    bookType,
    bookSST: true,
    type: 'array'
  })
  saveAs(new Blob([wbOut], { type: 'application/octet-stream' }),
    `${filename}.${bookType}`)
}
