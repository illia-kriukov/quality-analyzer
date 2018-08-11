package se.lnu.qualityanalyzer;

import org.eclipse.jgit.api.errors.GitAPIException;
import se.lnu.qualityanalyzer.enums.CVSType;
import se.lnu.qualityanalyzer.model.analysis.CommitAnalysisInput;
import se.lnu.qualityanalyzer.model.analysis.CommitAnalysisOutput;
import se.lnu.qualityanalyzer.service.analysis.QualityAnalyzer;
import se.lnu.qualityanalyzer.service.analysis.impl.QualityAnalyzerImpl;
import se.lnu.qualityanalyzer.service.report.ReportService;
import se.lnu.qualityanalyzer.service.report.impl.XLSXCommitReportService;

import java.util.ArrayList;
import java.util.List;

public class Application {

    public static void main(String[] args) throws GitAPIException {
        String repositoryName = "my-manuals-server";
        String repositoryUrl = "https://github.com/illia-kriukov/my-manuals-server.git";
        String repositoryOwner = "illia-kriukov";
        String branchName = "master";

//        String repositoryName = "commons-beanutils";
//        String repositoryUrl = "https://github.com/apache/commons-beanutils.git";
//        String repositoryOwner = "apache";
//        String branchName = "trunk";

//        String repositoryName = "junit4";
//        String repositoryUrl = "https://github.com/junit-team/junit4";
//        String repositoryOwner = "junit-team";
//        String branchName = "master";

        int numberOfCommits = 3;
        int offsetOfCommits = 0;

        QualityAnalyzer analyzer = new QualityAnalyzerImpl();
        CommitAnalysisInput analysisInput = new CommitAnalysisInput(CVSType.GIT_HUB, repositoryName, repositoryUrl, repositoryOwner,
                branchName, numberOfCommits, offsetOfCommits);
        List<CommitAnalysisOutput> analysisOutput = analyzer.analyzeCommits(analysisInput);

        printResults(analysisOutput);
        printResultsIntoFile(analysisInput, analysisOutput);
    }

    private static void printResults(List<CommitAnalysisOutput> analysisOutput) {
        analysisOutput.stream().forEach(o -> {
            System.out.println("\nCommitInfo: " + o.getCommitInfo().getSha() + "\nMetrics:");
            new ArrayList<>(o.getMetrics().values()).stream().forEach(m -> System.out.println(m.getName() + " = " + m.getValue()));
        });
    }

    private static void printResultsIntoFile(CommitAnalysisInput analysisInput, List<CommitAnalysisOutput> analysisOutput) {
        ReportService reportService = new XLSXCommitReportService();
        reportService.create(analysisInput, analysisOutput);
    }

}
