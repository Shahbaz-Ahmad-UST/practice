import { test as base, expect } from "@playwright/test";
import fs from "fs";
import path from "path";
import { logger, AppLogger } from "../src/logger";

type LogEntry = {
  step: number;
  timestamp: string;
  message: string;
};

type DiagnosticFixtures = {
  log: AppLogger;
};

export const test = base.extend<DiagnosticFixtures>({
  log: async ({ }, use, testInfo) => {
    const timeline: LogEntry[] = [];
    let step = 1;

    const diagnosticLog = logger.child({});
    // const originalInfo = diagnosticLog.info.bind(diagnosticLog);
    diagnosticLog.info = ((message: string) => {
      timeline.push({
        step: step++,
        timestamp: new Date().toLocaleTimeString("en-GB", {
          hour12: false,
        }),
        message,
      });

      // originalInfo(message);
      return diagnosticLog;
    }) as any;

    diagnosticLog.info("Test Started");

    await use(diagnosticLog);

    diagnosticLog.info(`Test ${testInfo.status}`);

    console.log(`\nExecution Timeline — ${testInfo.title}\n`);

    console.table(
      timeline.map((entry) => ({
        Step: entry.step,
        Timestamp: entry.timestamp,
        Message: entry.message,
      }))
    );

    const txtContent = timeline
      .map(
        (entry) =>
          `${entry.step}. [${entry.timestamp}] ${entry.message}`
      )
      .join("\n");

    const logsDir = path.join(process.cwd(), "logs");

    fs.mkdirSync(logsDir, { recursive: true });

    const fileName = `${testInfo.title.replace(/\s+/g, "_")}-log.txt`;

    fs.writeFileSync(
      path.join(logsDir, fileName),
      txtContent,
      "utf8"
    );

    await testInfo.attach("execution-log.txt", {
      body: txtContent,
      contentType: "text/plain",
    });
  },
});

export { expect };