package com.moonlightcheese

import java.io.File

fun main() {
    val inputFile = File("BC125_xMOTO_AMERICA_2024.bc125at_ss")
    val lines = inputFile.readLines()

    val channels: MutableList<Channel> = mutableListOf()

    lines.forEach { line ->
        if(line.startsWith("C-Freq")) {
            //val channel = line.substringAfter("C-Freq").substringBefore(Char(0x9))
            println(line)
            val ordinalIndex = nextIndex(line, 0)
            val ordinal = line.substring(ordinalIndex, line.indexOf(Char(0x9), ordinalIndex))
            println(ordinal)
            val nameIndex = nextIndex(line, ordinalIndex)
            val name = line.substring(nameIndex, line.indexOf(Char(0x9), nameIndex))
            println(name)
            val freqIndex = nextIndex(line, nameIndex)
            val freq = line.substring(freqIndex, line.indexOf(Char(0x9), freqIndex))
            println(freq)
            val modeIndex = nextIndex(line, freqIndex)
            val mode = line.substring(modeIndex, line.indexOf(Char(0x9), modeIndex))
            println(mode)
            val squelchIndex = nextIndex(line, modeIndex)
            val squelch = line.substring(squelchIndex, line.indexOf(Char(0x9), squelchIndex))
            val squelchType = squelch[0]
            println(squelchType)
            val squelchCode: String
            if(squelchType == 'D' || squelchType == 'C')
                squelchCode = squelch.substring(1)
            else
                squelchCode = "0"
            println(squelchCode)
            channels += Channel(
                ordinal.toInt(),
                freq,
                name,
                mode,
                squelchCode.toFloat(),
                squelchType
            )
        }
    }

    val rrName = inputFile.name.substringAfter("BC125 ").substringBefore(".bc125at_ss")
    val outputFile = File(rrName + "_RacingRadios.csv")
    outputFile.delete()
    outputFile.createNewFile()
    outputFile.appendText("Location,Name,Frequency,Duplex,Offset,Tone,rToneFreq,cToneFreq,DtcsCode,DtcsPolarity,RxDtcsCode,CrossMode,Mode,TStep,Skip,Power,Comment,URCALL,RPT1CALL,RPT2CALL,DVCODE\n")
    for((index, channel) in channels.withIndex()) {
        if(channel.frequency == "0")
            continue
        println(channel)
        var line = channel.ordinal.toString() + ","
        line += channel.name + ","
        //line += (channel.frequency / 1000000).toString() + ","
        val frequencyString = channel.frequency.toString()
        line += frequencyString.substring(0, 3) + "." + frequencyString.substring(3) + ","
        line += "off,"
        line += "0.000000,"
        if(channel.squelchType == 'D')
            line += "DTCS,"
        else if(channel.squelchType == 'C')
            line += "TSQL,"
        else
            line += ","
        line += "88.5,"
        if(channel.squelchType == 'C')
            line += channel.squelchCode.toString() + ","
        else
            line += "88.5,"
        if(channel.squelchType == 'D')
            line += channel.squelchCode.toInt().toString() + ","
        else
            line += "023,"
        line += "NN,"
        if(channel.squelchType == 'D')
            line += channel.squelchCode.toInt().toString() + ","
        else
            line += "023,"
        line += "Tone->Tone,"
        line += channel.mode + ","
        line += "5.00,,"
        line += "5.0W,"
        line += ",,,,"
        println(line)
        outputFile.appendText(line + "\n")
    }
}

fun nextIndex(line: String, index: Int): Int {
    return line.indexOf(Char(0x9), index) + 1
}