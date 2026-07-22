import { test as base, expect } from "@playwright/test";
import fs from "fs";
import path from "path";
import { logger, AppLogger } from "../src/logger";

type LogEntry = {
  timestamp: string;
  level: string;
  message: string;
};

type DiagnosticFixtures = {
  log: AppLogger;
};

export const test = base.extend<DiagnosticFixtures>({
  log: async ({}, use, testInfo) => {
    const timeline: LogEntry[] = [];

    const diagnosticLog = logger.child({});

    diagnosticLog.info = ((message: string) => {
      timeline.push({
        timestamp: new Date().toLocaleTimeString("en-GB", {
          hour12: false,
        }),
        level: "INFO",
        message,
      });

      return diagnosticLog;
    }) as any;

    diagnosticLog.warn = ((message: string) => {
      timeline.push({
        timestamp: new Date().toLocaleTimeString("en-GB", {
          hour12: false,
        }),
        level: "WARN",
        message,
      });

      return diagnosticLog;
    }) as any;

    diagnosticLog.error = ((message: string) => {
      timeline.push({
        timestamp: new Date().toLocaleTimeString("en-GB", {
          hour12: false,
        }),
        level: "ERROR",
        message,
      });

      return diagnosticLog;
    }) as any;

    diagnosticLog.info("Test Started");

    await use(diagnosticLog);

    diagnosticLog.info(`Test ${testInfo.status}`);

    console.log(
      `\nExecution Timeline — ${testInfo.title}\n`
    );

    console.table(
      timeline.map((entry) => ({
        Level: entry.level,
        Timestamp: entry.timestamp,
        Message: entry.message,
      }))
    );

    const txtContent = timeline
      .map(
        (entry, index) =>
          `${index + 1}. [${entry.timestamp}] ${entry.level} ${entry.message}`
      )
      .join("\n");

    const logsDir = path.join(
      process.cwd(),
      "logs"
    );

    fs.mkdirSync(logsDir, {
      recursive: true,
    });

    const fileName =
      `${testInfo.title.replace(/\s+/g, "_")}-log.txt`;

    fs.writeFileSync(
      path.join(logsDir, fileName),
      txtContent,
      "utf8"
    );

    await testInfo.attach(
      "execution-log.txt",
      {
        body: txtContent,
        contentType: "text/plain",
      }
    );
  },
});

export { expect };